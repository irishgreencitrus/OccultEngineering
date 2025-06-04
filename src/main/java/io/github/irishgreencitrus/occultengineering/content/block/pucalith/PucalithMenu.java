package io.github.irishgreencitrus.occultengineering.content.block.pucalith;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.foundation.gui.menu.MenuBase;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringGuiTextures;
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
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class PucalithMenu extends MenuBase<PucalithBlockEntity> {

    private Slot inputClipboard;
    private Slot outputClipboard;
    private Slot inputSchematic;
    private Slot outputSchematic;


    protected PucalithMenu(MenuType<?> type, int id, Inventory inv, PucalithBlockEntity contentHolder) {
        super(type, id, inv, contentHolder);
    }

    public PucalithMenu(MenuType<?> type, int id, Inventory inv, FriendlyByteBuf extraData) {
        super(type, id, inv, extraData);
    }

    public static PucalithMenu create(int id, Inventory inv, PucalithBlockEntity be) {
        return new PucalithMenu(OccultEngineeringMenuTypes.PUCALITH.get(), id, inv, be);
    }

    @Override
    protected PucalithBlockEntity createOnClient(FriendlyByteBuf extraData) {
        ClientLevel world = Minecraft.getInstance().level;
        BlockEntity blockEntity = world.getBlockEntity(extraData.readBlockPos());
        if (blockEntity instanceof PucalithBlockEntity be) {
            be.readClient(extraData.readNbt());
            return be;
        }
        return null;
    }

    @Override
    protected void initAndReadInventory(PucalithBlockEntity pucalithBlockEntity) {
    }

    @Override
    protected void addSlots() {
        int slot = 0;
        inputSchematic = new SlotItemHandler(contentHolder.inventory, slot++, 55, 60) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return OccultEngineeringItems.PENTACLE_SCHEMATIC.isIn(stack);
            }
        };

        outputSchematic = new SlotItemHandler(contentHolder.inventory, slot++, 149, 60) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return false;
            }
        };

        inputClipboard = new SlotItemHandler(contentHolder.inventory, slot++, 113, 21) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return AllBlocks.CLIPBOARD.isIn(stack)
                        || stack.is(Items.BOOK) || stack.is(Items.WRITTEN_BOOK) || stack.is(Items.WRITABLE_BOOK);
            }
        };

        outputClipboard = new SlotItemHandler(contentHolder.inventory, slot, 153, 21) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return false;
            }
        };

        addSlot(inputSchematic);
        addSlot(outputSchematic);
        addSlot(inputClipboard);
        addSlot(outputClipboard);

        int inventoryY = OccultEngineeringGuiTextures.PUCALITH.getHeight() + 4 + 18;

        addPlayerSlots(27, inventoryY);
    }

    @Override
    protected void saveData(PucalithBlockEntity pucalithBlockEntity) {
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        Slot clickedSlot = getSlot(index);
        if (!clickedSlot.hasItem())
            return ItemStack.EMPTY;

        ItemStack stack = clickedSlot.getItem();

        if (index < 4) {
            moveItemStackTo(stack, 4, slots.size(), false);
        } else {
            moveItemStackTo(stack, 0, 1, false);
            moveItemStackTo(stack, 2, 3, false);
        }
        return ItemStack.EMPTY;
    }
}
