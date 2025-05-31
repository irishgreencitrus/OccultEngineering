package io.github.irishgreencitrus.occultengineering.content.block.pentacle_altar;

import com.google.common.collect.ImmutableList;
import com.klikli_dev.modonomicon.api.multiblock.Multiblock;
import com.klikli_dev.modonomicon.data.MultiblockDataManager;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.menu.AbstractSimiContainerScreen;
import com.simibubi.create.foundation.gui.widget.IconButton;
import com.simibubi.create.foundation.gui.widget.Label;
import com.simibubi.create.foundation.gui.widget.ScrollInput;
import com.simibubi.create.foundation.gui.widget.SelectionScrollInput;
import io.github.irishgreencitrus.occultengineering.content.pentacleschematics.packet.PentacleAltarConfirmPacket;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringBlocks;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringGuiTextures;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringPackets;
import net.createmod.catnip.gui.element.GuiGameElement;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PentacleAltarScreen extends AbstractSimiContainerScreen<PentacleAltarMenu> {
    private ScrollInput pentaclesArea;
    protected OccultEngineeringGuiTextures background;

    private final ItemStack renderedItem = OccultEngineeringBlocks.PENTACLE_ALTAR.asStack();
    private IconButton confirmButton;
    private List<Rect2i> extraAreas = Collections.emptyList();

    public PentacleAltarScreen(PentacleAltarMenu container, Inventory inv, Component title) {
        super(container, inv, title);
        background = OccultEngineeringGuiTextures.PENTACLE_ALTAR;
    }


    @Override
    protected void init() {
        setWindowSize(background.getWidth(), background.getHeight() + 4 + AllGuiTextures.PLAYER_INVENTORY.getHeight());
        setWindowOffset(-11, 8);
        super.init();
        int x = leftPos;
        int y = topPos + 2;
        // TODO: we might have an issue if other mods use Modonomicon's multiblocks as well,
        //  so we could add a way to distinguish between only pentacles
        Map<ResourceLocation, Multiblock> multiblocks = MultiblockDataManager.get().getMultiblocks();
        List<ResourceLocation> optionList = multiblocks
                .keySet()
                .stream()
                .sorted()
                .toList();
        List<? extends Component> visibleOptions = optionList
                .stream()
                .map(it -> Component.translatable("multiblock." + it.getNamespace() + "." + it.getPath()))
                .toList();
        // Form translation keys like "multiblock.occultengineering.craft_puca"

        var pentaclesLabel = new Label(x + 51, y + 26, Component.empty()).withShadow();
        pentaclesLabel.text = Component.empty();

        // TODO: this might have a problem if we can't find any pentacles.
        pentaclesArea = new SelectionScrollInput(x + 45, y + 21, 139, 18)
                .forOptions(visibleOptions)
                .titled(Component.literal("Select Pentacle"))
                .writingTo(pentaclesLabel);

        addRenderableWidgets(pentaclesArea, pentaclesLabel);

        confirmButton = new IconButton(x + 44, y + 56, AllIcons.I_CONFIRM);
        confirmButton.withCallback(() -> {
            if (menu.canWrite() && pentaclesArea != null) {
                // send packet to give player a pentacle schematic
                var pentacleLocation = optionList.get(pentaclesArea.getState());
                OccultEngineeringPackets.getChannel().sendToServer(new PentacleAltarConfirmPacket(pentacleLocation));
            }

        });
        addRenderableWidget(confirmButton);

        extraAreas = ImmutableList.of(
                new Rect2i(x + background.getWidth(), y + background.getHeight() - 40, 48, 48)
        );

    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        int invX = getLeftOfCentered(AllGuiTextures.PLAYER_INVENTORY.getWidth());
        int invY = topPos + background.getHeight() + 4;
        renderPlayerInventory(graphics, invX, invY);

        int x = leftPos;
        int y = topPos;
        background.render(graphics, x, y);

        Component titleText;
        titleText = Component.translatable("gui.occultengineering.pentacle_altar.title");
        graphics.drawString(font, titleText, x + (background.getWidth() - 8 - font.width(titleText)) / 2, y + 4, 0x505050, false);
        GuiGameElement.of(renderedItem)
                .<GuiGameElement.GuiRenderBuilder>at(x + background.getWidth(), y + background.getHeight() - 40, -200)
                .scale(3)
                .render(graphics);

    }

    @Override
    public List<Rect2i> getExtraAreas() {
        return extraAreas;
    }
}
