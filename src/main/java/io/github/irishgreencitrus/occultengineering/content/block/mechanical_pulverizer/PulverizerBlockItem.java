package io.github.irishgreencitrus.occultengineering.content.block.mechanical_pulverizer;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PulverizerBlockItem extends BlockItem {
    private static final String CRUSHING_ITEM_TIER = "CrushingItemTier";

    public PulverizerBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    public static int getTier(ItemStack stack) {
        return stack.hasTag() && stack.getTag().contains(CRUSHING_ITEM_TIER)
                ? stack.getTag().getInt(CRUSHING_ITEM_TIER)
                : 1;
    }

    public static void setTier(ItemStack stack, int tier) {
        if (tier <= 1) {
            if (!stack.hasTag()) return;
            stack.getTag().remove(CRUSHING_ITEM_TIER);
            if (stack.getTag().isEmpty()) stack.setTag(null);
            return;
        }
        stack.getOrCreateTag().putInt(CRUSHING_ITEM_TIER, tier);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents,
                                TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, level, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.translatable("occultengineering.tooltip.tier", getTier(stack))
                .withStyle(ChatFormatting.LIGHT_PURPLE));
    }
}
