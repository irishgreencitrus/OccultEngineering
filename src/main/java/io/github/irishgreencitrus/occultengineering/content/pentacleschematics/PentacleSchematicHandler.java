package io.github.irishgreencitrus.occultengineering.content.pentacleschematics;

import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Overlay;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;

import java.util.Optional;

/*
public class PentacleSchematicHandler implements IGuiOver {
    private boolean active = false;
    private ItemStack activeSchematicItem;
    private int activeHotbarSlot;

    public void tick() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.gameMode.getPlayerMode() == GameType.SPECTATOR) {
            if (active) {
                active = false;
                activeSchematicItem = null;
            }
        }

        LocalPlayer player = mc.player;
        assert player != null;
        Optional<ItemStack> stack = findPentacleSchematicInHand(player);
        if (stack.isEmpty()) {
            active = false;
        }
    }

    private Optional<ItemStack> findPentacleSchematicInHand(Player player) {
        var item = player.getMainHandItem();
        if (!OccultEngineeringItems.PENTACLE_SCHEMATIC.isIn(item))
            return Optional.empty();
        if (!item.hasTag())
            return Optional.empty();

        activeSchematicItem = item;
        activeHotbarSlot = player.getInventory().selected;
        return Optional.of(item);
    }

    private boolean itemLost(Player player) {
        for (int i = 0; i < Inventory.getSelectionSize(); i++) {
            if (player.getInventory()
                    .getItem(i)
                    .is(activeSchematicItem.getItem()))
                continue;
            if (!ItemStack.matches(player.getInventory()
                    .getItem(i), activeSchematicItem))
                continue;
            return false;
        }
        return true;
    }

    @Override
    public void render(ForgeGui forgeGui, GuiGraphics guiGraphics, float v, int i, int i1) {
        if (Minecraft.getInstance().options.hideGui || !active)
            return;
        // TODO: render a bounding box

    }
}
 */