package io.github.irishgreencitrus.occultengineering.content.block.pucalith;

import com.google.common.collect.ImmutableList;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.gui.menu.AbstractSimiContainerScreen;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringBlocks;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringGuiTextures;
import net.createmod.catnip.gui.element.GuiGameElement;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class PucalithScreen extends AbstractSimiContainerScreen<PucalithMenu> {
    protected OccultEngineeringGuiTextures background;


    private final ItemStack renderedItem = OccultEngineeringBlocks.PUCALITH.asStack();
    private List<Rect2i> extraAreas = Collections.emptyList();

    public PucalithScreen(PucalithMenu container, Inventory inv, Component title) {
        super(container, inv, title);
        background = OccultEngineeringGuiTextures.PUCALITH;
    }

    @Override
    protected void init() {
        setWindowSize(background.getWidth(), background.getHeight() + 4 + AllGuiTextures.PLAYER_INVENTORY.getHeight());
        setWindowOffset(-11, 8);
        super.init();
        int x = leftPos;
        int y = topPos + 2;

        extraAreas = ImmutableList.of(
                new Rect2i(x + background.getWidth(), y + background.getHeight() - 40, 48, 48)
        );
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics graphics, float v, int i, int i1) {
        int invX = getLeftOfCentered(AllGuiTextures.PLAYER_INVENTORY.getWidth());
        int invY = topPos + background.getHeight() + 4;
        renderPlayerInventory(graphics, invX, invY);

        int x = leftPos;
        int y = topPos;
        background.render(graphics, x, y);

        PucalithBlockEntity be = menu.contentHolder;
        float amount = (float) be.getTankUsage() / be.getTankCapacity();
        renderFuelTank(graphics, x, y, amount);

        Component titleText;
        titleText = Component.translatable("gui.occultengineering.pucalith.title");
        graphics.drawString(font, titleText, x + (background.getWidth() - 8 - font.width(titleText)) / 2, y + 4, 0x505050, false);

        GuiGameElement.of(renderedItem)
                .<GuiGameElement.GuiRenderBuilder>at(x + background.getWidth(), y + background.getHeight() - 40, -200)
                .scale(3)
                .render(graphics);
    }

    private void renderFuelTank(GuiGraphics graphics, int x, int y, float amount) {
        OccultEngineeringGuiTextures sprite = OccultEngineeringGuiTextures.PUCALITH_FULL_TANK;
        if (menu.contentHolder.hasCreativeCrate) {
            sprite = OccultEngineeringGuiTextures.PUCALITH_CREATIVE_TANK;
        }
        int fillHeight = (int) Math.floor(sprite.getHeight() * amount);
        graphics.blit(sprite.location, x + 10, y + 22 + (sprite.getHeight() - fillHeight), sprite.getStartX(), sprite.getStartY() + (sprite.getHeight() - fillHeight), sprite.getWidth(), fillHeight);
    }

    @Override
    public List<Rect2i> getExtraAreas() {
        return extraAreas;
    }
}
