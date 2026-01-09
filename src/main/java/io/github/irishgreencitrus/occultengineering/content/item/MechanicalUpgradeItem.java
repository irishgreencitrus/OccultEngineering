package io.github.irishgreencitrus.occultengineering.content.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class MechanicalUpgradeItem extends Item {
    private final int tier;
    public MechanicalUpgradeItem(int tier, Properties properties) {
        super(properties);

        this.tier = tier;
    }

    public int getTier() {
        return tier;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

        tooltipComponents.add(Component.literal("Tier: " + tier).withStyle(ChatFormatting.LIGHT_PURPLE));
    }
}
