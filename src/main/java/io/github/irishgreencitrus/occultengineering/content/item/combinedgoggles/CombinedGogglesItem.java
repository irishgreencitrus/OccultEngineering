package io.github.irishgreencitrus.occultengineering.content.item.combinedgoggles;

import com.klikli_dev.occultism.common.item.armor.OtherworldGogglesItem;
import com.klikli_dev.occultism.registry.OccultismDataComponents;
import com.simibubi.create.content.equipment.goggles.GogglesItem;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringItems;
import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CombinedGogglesItem extends Item implements Equipable {
    public CombinedGogglesItem(Properties properties) {
        super(properties);
    }

    static {
        GogglesItem.addIsWearingPredicate(player -> OccultEngineeringItems.COMBINED_GOGGLES.isIn(player.getItemBySlot(EquipmentSlot.HEAD)));
    }

    @Override
    public EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.HEAD;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return OtherworldGogglesItem.isGogglesItem(stack) || super.isFoil(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if (OtherworldGogglesItem.isGogglesItem(stack)) {
            tooltipComponents.add(Component.translatable("item.occultengineering.combined_goggles.otherworld_enabled").withStyle(ChatFormatting.LIGHT_PURPLE));
        } else {
            tooltipComponents.add(Component.translatable("item.occultengineering.combined_goggles.otherworld_disabled").withStyle(ChatFormatting.GRAY));
        }
        tooltipComponents.add(Component.translatable("item.occultengineering.combined_goggles.use_to_change"));
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        var stack = player.getItemInHand(usedHand);
        if (player.isShiftKeyDown()) {
            return this.swapWithEquipmentSlot(stack.getItem(), level, player, usedHand);
        }
        stack.set(OccultismDataComponents.OTHERWORLD_GOGGLES, !OtherworldGogglesItem.isGogglesItem(stack));
        return InteractionResultHolder.success(stack);
    }
}
