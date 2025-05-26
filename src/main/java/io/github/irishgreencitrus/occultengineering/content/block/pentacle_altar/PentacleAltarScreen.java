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

/*
TODO: finish this class.
    It should look similar to the Schematic Table, but choosing pentacles instead, obviously.
    One Input for empty pentacle schematics, one output for complete ones.
    You can click to place them on the ground just like a regular Create schematic.
    Let's not use the Schematicannon to place them in survival though, we could add another input to the table
    to place them. Instead of using gunpowder we could summon a Púca mob that would place the pentacles block by block for you.
    Maybe include pumping spirit solution into the table, just to add another use for it?
    As for buttons, we need a confirm button and that's basically it (considering pentacles won't be added after the game is running).
    Maybe a Púca button to spawn the Púca?
    We could combine the functionality of the schematicannon and the Schematic table in here, as we have less functionality
 */
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
        titleText = Component.literal("Done!");
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
