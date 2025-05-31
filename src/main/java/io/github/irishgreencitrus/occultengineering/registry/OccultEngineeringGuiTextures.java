package io.github.irishgreencitrus.occultengineering.registry;

import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import net.createmod.catnip.gui.TextureSheetSegment;
import net.createmod.catnip.gui.element.ScreenElement;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public enum OccultEngineeringGuiTextures implements ScreenElement, TextureSheetSegment {
    PENTACLE_ALTAR("schematics", 10, 139, 214, 85),
    PUCALITH("schematics", 10, 8, 192, 123),
    PUCALITH_CREATIVE_TANK("schematics", 208, 32, 16, 64),
    PUCALITH_FULL_TANK("schematics", 224, 32, 16, 64),
    ;
    public static final int FONT_COLOR = 0x575F7A;

    public final ResourceLocation location;
    private final int width;
    private final int height;
    private final int startX;
    private final int startY;

    OccultEngineeringGuiTextures(String location, int width, int height) {
        this(location, 0, 0, width, height);
    }

    OccultEngineeringGuiTextures(String location, int startX, int startY, int width, int height) {
        this(OccultEngineering.MODID, location, startX, startY, width, height);
    }

    OccultEngineeringGuiTextures(String namespace, String location, int startX, int startY, int width, int height) {
        this.location = new ResourceLocation(namespace, "textures/gui/" + location + ".png");
        this.width = width;
        this.height = height;
        this.startX = startX;
        this.startY = startY;
    }

    @Override
    public int getStartX() {
        return startX;
    }

    @Override
    public int getStartY() {
        return startY;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void render(GuiGraphics guiGraphics, int x, int y) {
        guiGraphics.blit(location, x, y, startX, startY, width, height);

    }

    @Override
    public @NotNull ResourceLocation getLocation() {
        return location;
    }
}
