package io.github.irishgreencitrus.occultengineering.content.item.combinedgoggles;

import com.klikli_dev.occultism.common.item.armor.OtherworldGogglesItem;
import com.simibubi.create.content.equipment.goggles.GogglesItem;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringItems;
import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.ByteTag;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

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
        return isOtherworldly(stack) || super.isFoil(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        if (isOtherworldly(stack)) {
            tooltipComponents.add(Component.translatable("item.occultengineering.combined_goggles.otherworld_enabled").withStyle(ChatFormatting.LIGHT_PURPLE));
        } else {
            tooltipComponents.add(Component.translatable("item.occultengineering.combined_goggles.otherworld_disabled").withStyle(ChatFormatting.GRAY));
        }
        tooltipComponents.add(Component.translatable("item.occultengineering.combined_goggles.use_to_change"));
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        var stack = player.getItemInHand(usedHand);
        if (player.isShiftKeyDown()) {
            return this.swapWithEquipmentSlot(stack.getItem(), level, player, usedHand);
        }
        stack.addTagElement(OtherworldGogglesItem.NBT_GOGGLES, ByteTag.valueOf(!isOtherworldly(stack)));
        return InteractionResultHolder.success(stack);
    }

    protected boolean isOtherworldly(ItemStack stack) {
        if (stack.getTag() == null) return false;
        if (stack.getTag().get(OtherworldGogglesItem.NBT_GOGGLES) == null) return false;
        return stack.getTag().getBoolean(OtherworldGogglesItem.NBT_GOGGLES);
    }
}
