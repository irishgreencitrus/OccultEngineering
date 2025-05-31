package io.github.irishgreencitrus.occultengineering.content.block.pucalith;

import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.utility.IInteractionChecker;
import io.github.irishgreencitrus.occultengineering.content.fluid.FilteredFluidTankBehaviour;
import io.github.irishgreencitrus.occultengineering.content.pentacleschematics.PentacleMaterialChecklist;
import io.github.irishgreencitrus.occultengineering.content.pentacleschematics.PentaclePrinter;
import io.github.irishgreencitrus.occultengineering.content.pentacleschematics.PentacleSchematic;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringTags;
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
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PucalithBlockEntity extends SmartBlockEntity implements MenuProvider, IInteractionChecker, IHaveGoggleInformation {
    public PucalithInventory inventory;

    // fluid
    private SmartFluidTankBehaviour internalTank;

    // printer
    public PentaclePrinter printer;
    public PentacleMaterialChecklist checklist;
    private boolean updateChecklist = false;
    public BlockPos previousTarget;
    public boolean hasCreativeCrate = false;

    public final int MAX_TANK_CAPACITY_MB = 2000;

    public PucalithBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        inventory = new PucalithInventory();
        printer = new PentaclePrinter();
        checklist = new PentacleMaterialChecklist();
        internalTank.getPrimaryTank().getTotalUnits(0);
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

        // TODO: search for inventories near the pucalith
        if (level.isClientSide)
            return;

        tickBookPrinter();
    }

    public void tickBookPrinter() {
        int clipboardIn = 2;
        int clipboardOut = 3;

        ItemStack schematicItem = inventory.getStackInSlot(0);
        ItemStack clipboard = inventory.extractItem(clipboardIn, 1, true);
        var outputFull = inventory.getStackInSlot(clipboardOut).getCount() == inventory.getSlotLimit(clipboardOut);

        if (!printer.isInitialised()) {
            if (!schematicItem.isEmpty()) {
                var schem = PentacleSchematic.fromStack(level, schematicItem);
                schem.ifPresent(schematic -> {
                    checklist = new PentacleMaterialChecklist();
                    checklist.require(schematic.getItemRequirement());

                    printer.initialise(schematic);
                });
            }
            return;
        }

        if (clipboard.isEmpty() || outputFull) {
            updateChecklist = true;
            return;
        }

        // TODO: add a short timer to this, like the schematicannon
        if (updateChecklist) {
            updateChecklist();
        }

        updateChecklist = false;
        ItemStack extractedItem = inventory.extractItem(clipboardIn, 1, false);

        // TODO: don't assume the checklist is a clipboard.
        ItemStack stack = checklist.createWrittenClipboard();

        stack.setCount(inventory.getStackInSlot(clipboardOut).getCount() + 1);
        inventory.setStackInSlot(clipboardOut, stack);
    }

    public void updateChecklist() {
        // TODO: update the checklist based on items in attached inventories

    }

    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
        inventory.deserializeNBT(tag.getCompound("Inventory"));
        super.read(tag, clientPacket);
    }

    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        tag.put("Inventory", inventory.serializeNBT());
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

    public int getTankUsage() {
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
