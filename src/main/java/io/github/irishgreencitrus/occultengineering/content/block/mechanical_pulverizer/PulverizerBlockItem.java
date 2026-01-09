package io.github.irishgreencitrus.occultengineering.content.block.mechanical_pulverizer;

import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class PulverizerBlockItem extends BlockItem {
    public PulverizerBlockItem(Block block, Properties properties) {
        super(block, properties.component(OccultEngineeringDataComponents.CRUSHING_ITEM_TIER, 1));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        int tier = stack.getOrDefault(OccultEngineeringDataComponents.CRUSHING_ITEM_TIER, 1);

        tooltipComponents.add(Component.literal("Tier: " + tier).withStyle(ChatFormatting.LIGHT_PURPLE));
    }
}
