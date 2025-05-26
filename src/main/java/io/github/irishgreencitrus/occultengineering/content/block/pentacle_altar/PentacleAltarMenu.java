package io.github.irishgreencitrus.occultengineering.content.block.pentacle_altar;

import com.simibubi.create.foundation.gui.menu.MenuBase;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringItems;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringMenuTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class PentacleAltarMenu extends MenuBase<PentacleAltarBlockEntity> {
    private Slot inputSlot;
    private Slot outputSlot;

    public PentacleAltarMenu(MenuType<?> type, int id, Inventory inv, FriendlyByteBuf extraData) {
        super(type, id, inv, extraData);
    }

    public PentacleAltarMenu(MenuType<?> type, int id, Inventory inv, PentacleAltarBlockEntity contentHolder) {
        super(type, id, inv, contentHolder);
    }

    public static PentacleAltarMenu create(int id, Inventory inv, PentacleAltarBlockEntity be) {
        return new PentacleAltarMenu(OccultEngineeringMenuTypes.PENTACLE_ALTAR.get(), id, inv, be);
    }

    public boolean canWrite() {
        return inputSlot.hasItem() && !outputSlot.hasItem();
    }

    @Override
    protected PentacleAltarBlockEntity createOnClient(FriendlyByteBuf extraData) {
        ClientLevel world = Minecraft.getInstance().level;
        BlockEntity blockEntity = world.getBlockEntity(extraData.readBlockPos());
        if (blockEntity instanceof PentacleAltarBlockEntity be) {
            be.readClient(extraData.readNbt());
            return be;
        }
        return null;
    }

    @Override
    protected void initAndReadInventory(PentacleAltarBlockEntity pentacleAltarBlockEntity) {

    }

    @Override
    protected void addSlots() {
        inputSlot = new SlotItemHandler(contentHolder.inventory, 0, 21, 59) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return OccultEngineeringItems.EMPTY_PENTACLE_SCHEMATIC.isIn(stack);
            }
        };

        outputSlot = new SlotItemHandler(contentHolder.inventory, 1, 166, 59) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return false;
            }
        };

        addSlot(inputSlot);
        addSlot(outputSlot);

        // player inventory
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(player.getInventory(), col + row * 9 + 9, 38 + col * 18, 107 + row * 18));
            }
        }

        for (int hotbarSlot = 0; hotbarSlot < 9; ++hotbarSlot) {
            this.addSlot(new Slot(player.getInventory(), hotbarSlot, 38 + hotbarSlot * 18, 165));
        }

    }

    @Override
    protected void saveData(PentacleAltarBlockEntity pentacleAltarBlockEntity) {

    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot clickedSlot = getSlot(index);
        if (!clickedSlot.hasItem())
            return ItemStack.EMPTY;

        ItemStack stack = clickedSlot.getItem();
        if (index < 2) {
            moveItemStackTo(stack, 2, slots.size(), false);
        } else {
            moveItemStackTo(stack, 0, 1, false);
        }
        return ItemStack.EMPTY;
    }
}
