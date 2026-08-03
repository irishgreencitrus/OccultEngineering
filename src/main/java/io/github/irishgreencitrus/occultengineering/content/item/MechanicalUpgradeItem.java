package io.github.irishgreencitrus.occultengineering.content.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

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
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents,
                                TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, level, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.translatable("occultengineering.tooltip.tier", tier)
                .withStyle(ChatFormatting.LIGHT_PURPLE));
    }
}
