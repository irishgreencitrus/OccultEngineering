package io.github.irishgreencitrus.occultengineering.content.item;

import com.klikli_dev.modonomicon.data.MultiblockDataManager;
import com.simibubi.create.AllDataComponents;
import io.github.irishgreencitrus.occultengineering.content.pentacleschematics.PentaclePrinter;
import io.github.irishgreencitrus.occultengineering.content.pentacleschematics.PentacleSchematic;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringDataComponents;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringItems;
import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class PentacleSchematicItem extends Item {
    public PentacleSchematicItem(Properties properties) {
        super(properties);
    }

    public static ItemStack create(ResourceLocation pentacleLocation) {
        var blueprint = OccultEngineeringItems.PENTACLE_SCHEMATIC.asStack();

        // We're reusing some of Create's data components purely because it's easier
        blueprint.set(AllDataComponents.SCHEMATIC_DEPLOYED, false);
        blueprint.set(OccultEngineeringDataComponents.PENTSCHEM_RESOURCE_LOCATION, pentacleLocation);
        blueprint.set(AllDataComponents.SCHEMATIC_ANCHOR, BlockPos.ZERO);
        blueprint.set(OccultEngineeringDataComponents.PENTSCHEM_BOUNDS, MultiblockDataManager.get().getMultiblock(pentacleLocation).getSize());
        return blueprint;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if (stack.has(OccultEngineeringDataComponents.PENTSCHEM_RESOURCE_LOCATION)) {
            var pentacleResource = stack.get(OccultEngineeringDataComponents.PENTSCHEM_RESOURCE_LOCATION);
            if (pentacleResource != null)
                tooltipComponents.add(
                        Component.translatable("item.occultengineering.pentacle_schematic.tooltip_prefix")
                                .append(Component.translatable("multiblock." + pentacleResource.getNamespace() + "." + pentacleResource.getPath())
                                        .withStyle(ChatFormatting.RED)));
        }
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    private static PentaclePrinter printer;

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        var stack = player.getItemInHand(usedHand);
        if (usedHand != InteractionHand.MAIN_HAND)
            return InteractionResultHolder.fail(stack);

        if (!stack.has(OccultEngineeringDataComponents.PENTSCHEM_RESOURCE_LOCATION)) return InteractionResultHolder.fail(stack);

        if (player.isShiftKeyDown() && player.onGround()) {
            if (player.isCreative()) {
                if (stack.has(AllDataComponents.SCHEMATIC_DEPLOYED) && stack.has(AllDataComponents.SCHEMATIC_ANCHOR)) {
                    var deployed = stack.get(AllDataComponents.SCHEMATIC_DEPLOYED);
                    var tagPos = stack.get(AllDataComponents.SCHEMATIC_ANCHOR);
                    if (deployed == null || tagPos == null) return InteractionResultHolder.fail(stack);

                    if (deployed && tagPos.equals(player.blockPosition())) {
                        var schem = PentacleSchematic.fromStack(level, stack);
                        if (schem.left().isEmpty()) {
                            return InteractionResultHolder.fail(stack);
                        }
                        schem.orThrow().instantPlace();
                        return InteractionResultHolder.success(stack);
                    }
                }
                player.displayClientMessage(
                        Component.translatable("gui.occultengineering.pentacle_schematic.place_hint_creative", player.blockPosition().toShortString())
                                .withStyle(ChatFormatting.GREEN),
                        true);
            } else {
                player.displayClientMessage(
                        Component.translatable("gui.occultengineering.pentacle_schematic.place_hint", player.blockPosition().toShortString())
                                .withStyle(ChatFormatting.GREEN),
                        true);
            }
            stack.set(AllDataComponents.SCHEMATIC_ANCHOR, player.blockPosition());
            stack.set(AllDataComponents.SCHEMATIC_DEPLOYED, true);
        }

        return InteractionResultHolder.success(stack);
    }
}
