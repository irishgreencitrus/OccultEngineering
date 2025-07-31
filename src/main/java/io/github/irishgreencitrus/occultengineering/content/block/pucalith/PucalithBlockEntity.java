package io.github.irishgreencitrus.occultengineering.content.block.pucalith;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.utility.IInteractionChecker;
import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import io.github.irishgreencitrus.occultengineering.content.fluid.FilteredFluidTankBehaviour;
import io.github.irishgreencitrus.occultengineering.content.pentacleschematics.PentacleMaterialChecklist;
import io.github.irishgreencitrus.occultengineering.content.pentacleschematics.PentaclePrinter;
import io.github.irishgreencitrus.occultengineering.content.pentacleschematics.PentacleSchematic;
import io.github.irishgreencitrus.occultengineering.content.pentacleschematics.PentacleSchematic.ParseResult;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringTags;
import net.createmod.catnip.data.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.EmptyHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashSet;
import java.util.List;

public class PucalithBlockEntity extends SmartBlockEntity implements MenuProvider, IInteractionChecker, IHaveGoggleInformation {
    public static final int NEIGHBOUR_CHECK_MAX = 100;

    public enum State {
        STOPPED,
        PAUSED,
        RUNNING
    }

    public State state;

    public PucalithInventory inventory;

    // fluid
    private SmartFluidTankBehaviour internalTank;

    // printer
    public PentaclePrinter printer;
    public PentacleSchematic schematic;
    public ParseResult schematicParseResult;
    public int printerCooldown;
    public PentacleMaterialChecklist checklist;

    // sync
    public boolean sendUpdate = false;
    public boolean shouldUpdateChecklist = false;
    public int neighborCheckCooldown = 0;

    public BlockPos previousTarget;
    public boolean hasCreativeCrate = false;
    public LinkedHashSet<LazyOptional<IItemHandler>> attachedInventories;

    public final int MAX_TANK_CAPACITY_MB = 2000;

    public PucalithBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        inventory = new PucalithInventory();
        printer = new PentaclePrinter();
        checklist = new PentacleMaterialChecklist();
        attachedInventories = new LinkedHashSet<>();
        internalTank.getPrimaryTank().getTotalUnits(0);
        this.state = State.STOPPED;
    }

    public class PucalithInventory extends ItemStackHandler {
        public PucalithInventory() {
            // Input blueprint,
            // Output blueprint.
            // Input clipboard,
            // Output clipboard
            super(4);
        }

        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            setChanged();
        }
    }


    @Override
    public void tick() {
        super.tick();

        if (neighborCheckCooldown-- <= 0) {
            neighborCheckCooldown = NEIGHBOUR_CHECK_MAX;
            findInventories();
        }

        if (level == null || level.isClientSide)
            return;

        tickBookPrinter();

        for (int skipsLeft = 1000; skipsLeft >= 0; skipsLeft--) {
            var shouldTryAgain = tickPentaclePrinter();
            if (!shouldTryAgain) break;
        }

        if (sendUpdate) {
            sendUpdate = false;
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 6);
        }
    }

    //region Book Printer

    protected void tickBookPrinter() {
        int clipboardIn = 2;
        int clipboardOut = 3;

        ItemStack schematicItem = inventory.getStackInSlot(0);
        ItemStack clipboard = inventory.extractItem(clipboardIn, 1, true);
        var outputFull = inventory.getStackInSlot(clipboardOut).getCount() == inventory.getSlotLimit(clipboardOut);

        if (!printer.isInitialised()) {
            if (!schematicItem.isEmpty()) {
                var schem = PentacleSchematic.fromStack(level, schematicItem);
                schem.ifLeft(schematic -> {
                    this.schematic = schematic;
                    checklist = new PentacleMaterialChecklist();
                    checklist.require(schematic.getItemRequirement());

                    printer.initialise(schematic);
                });
            }
            return;
        }

        if (clipboard.isEmpty() || outputFull) {
            shouldUpdateChecklist = true;
            return;
        }

        // TODO: add a short timer to this, like the schematicannon
        if (shouldUpdateChecklist) {
            updateChecklist();
        }

        shouldUpdateChecklist = false;
        ItemStack extractedItem = inventory.extractItem(clipboardIn, 1, false);

        ItemStack stack = AllBlocks.CLIPBOARD.isIn(extractedItem) ? checklist.createWrittenClipboard() : checklist.createWrittenBook();

        stack.setCount(inventory.getStackInSlot(clipboardOut).getCount() + 1);
        inventory.setStackInSlot(clipboardOut, stack);
    }

    public void updateChecklist() {
        checklist.clear();

        if (schematic == null) return;
        if (printer.isInitialised()) {
            checklist.require(printer.getStillToPlaceRequrement());
        } else {
            checklist.require(schematic.getItemRequirement());
        }

        findInventories();

        for (var cap : attachedInventories) {
            if (!cap.isPresent()) continue;

            IItemHandler inventory = cap.orElse(EmptyHandler.INSTANCE);

            for (int slot = 0; slot < inventory.getSlots(); slot++) {
                ItemStack stack = inventory.getStackInSlot(slot);
                if (inventory.extractItem(slot, 1, true).isEmpty()) continue;

                checklist.collect(stack);
            }
        }
        sendUpdate = true;
    }

    //endregion

    //region Pentacle Printer
    public void findInventories() {
        hasCreativeCrate = false;
        attachedInventories.clear();
        for (Direction face : Iterate.directions) {
            var rel = worldPosition.relative(face);
            if (level == null || !level.isLoaded(rel)) continue;

            if (AllBlocks.CREATIVE_CRATE.has(level.getBlockState(rel))) {
                hasCreativeCrate = true;
                return;
            }

            var be = level.getBlockEntity(rel);
            if (be == null) continue;

            LazyOptional<IItemHandler> itemHandler = be.getCapability(ForgeCapabilities.ITEM_HANDLER, face.getOpposite());
            if (itemHandler.isPresent()) {
                attachedInventories.add(itemHandler);
            }
        }
    }

    /**
     * @return Whether we should try placing a block again this tick (when skips are implemented properly)
     */
    protected boolean tickPentaclePrinter() {
        assert level != null;
        assert !level.isClientSide();

        ItemStack blueprintItem = inventory.getStackInSlot(0);

        if (blueprintItem.isEmpty() && state != State.STOPPED && inventory.getStackInSlot(1).isEmpty()) {
            state = State.STOPPED;
            sendUpdate = true;
            return false;
        }

        if (state == State.STOPPED) {
            if (printer.isInitialised())
                resetPrinter();
            return false;
        }

        if (!printer.isInitialised()) {
            initPrinter(blueprintItem);
            return false;
        }

        if (!level.isLoaded(schematic.position)) {
            return false;
        }

        if (printerCooldown-- > 0) return false;

        // spawn or find the puca if it doesn't exist
        // TODO: scrap using the púca to perform this and make it more magical with a bunch of particles.
        //       as this will be way easier to handle. Mildly annoying as I feel I am 95% there.
        //       Also repurpose the Púca to do something else?
        //       Pray this doesn't get completely superseded in 1.21's Occultism


        if (getTankRemaining() <= 0) {
            return false;
        }

        if (!printer.hasNextPlace()) return false;

        var nextToPlace = printer.popNextToPlace();

        OccultEngineering.LOGGER.warn("Placing {} next", nextToPlace.toString());

        if (nextToPlace.getSecond().left().isEmpty()) return false;

        var pos = nextToPlace.getFirst();
        var state = nextToPlace.getSecond().left().orElseThrow();

        // TODO: load from config.
        printerCooldown = 20;


        var requirement = printer.getStillToPlaceRequrement();
        // TODO: check we can place the next block
        // TODO: pick out our next item from the inventory
        sendUpdate = true;
        return false;
    }

    protected void initPrinter(ItemStack stack) {
        var schem = PentacleSchematic.fromStack(level, stack);
        sendUpdate = true;
        if (schem.left().isEmpty()) {
            // we had some sort of error loading the schematic
            state = State.STOPPED;
            schematicParseResult = schem.right().orElseThrow();
            return;
        }

        schematic = schem.left().get();
        printer.initialise(schematic);

        state = State.PAUSED;
        schematicParseResult = ParseResult.OK;
    }

    public void resetPrinter() {
        printer.deinit();
        checklist.clear();
        schematic = null;

    }
    //endregion

    public void onPlayButton() {
        state = State.RUNNING;
    }

    public void onPauseButton() {
        state = State.PAUSED;
    }

    public void onStopButton() {
        state = State.STOPPED;
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        findInventories();
    }

    private final String INVENTORY_TAG = "Inventory";
    private final String PUCA_UUID_TAG = "PucaUUID";
    private final String CURRENT_STATE_TAG = "CurrentState";

    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
        inventory.deserializeNBT(tag.getCompound(INVENTORY_TAG));
        if (tag.contains(CURRENT_STATE_TAG))
            state = State.valueOf(tag.getString(CURRENT_STATE_TAG));

        super.read(tag, clientPacket);
    }

    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        tag.put(INVENTORY_TAG, inventory.serializeNBT());
        tag.putString(CURRENT_STATE_TAG, state.toString());

        super.write(tag, clientPacket);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(internalTank = FilteredFluidTankBehaviour.single(
                (fluidStack) -> fluidStack.getFluid().is(OccultEngineeringTags.PUCALITH_FUEL),
                this,
                MAX_TANK_CAPACITY_MB
        ).allowInsertion().forbidExtraction());
    }

    public int getTankRemaining() {
        if (hasCreativeCrate) return getTankCapacity();
        return (int) internalTank.getPrimaryTank().getTotalUnits(0);
    }

    public int getTankCapacity() {
        return MAX_TANK_CAPACITY_MB;
    }

    @Override
    public Component getDisplayName() {
        return Component.empty();
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return PucalithMenu.create(i, inventory, this);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (side != Direction.UP && isFluidHandlerCap(cap))
            return internalTank.getCapability().cast();
        return super.getCapability(cap, side);
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        return containedFluidTooltip(tooltip, isPlayerSneaking, getCapability(ForgeCapabilities.FLUID_HANDLER));
    }
}
