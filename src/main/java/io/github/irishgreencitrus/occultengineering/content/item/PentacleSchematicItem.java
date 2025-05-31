package io.github.irishgreencitrus.occultengineering.content.item;

import com.klikli_dev.modonomicon.data.MultiblockDataManager;
import io.github.irishgreencitrus.occultengineering.content.pentacleschematics.PentaclePrinter;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringItems;
import net.createmod.catnip.nbt.NBTHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

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

        CompoundTag tag = new CompoundTag();
        tag.putBoolean("Deployed", false);
        tag.putString("Pentacle", pentacleLocation.toString());
        tag.put("Position", NbtUtils.writeBlockPos(BlockPos.ZERO));
        tag.put("Bounds", NBTHelper.writeVec3i(MultiblockDataManager.get().getMultiblock(pentacleLocation).getSize()));

        blueprint.setTag(tag);
        return blueprint;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        if (stack.hasTag()) {
            if (stack.getTag().contains("Pentacle")) {
                var pentacleTag = stack.getTag().getString("Pentacle");
                var pentacleResource = ResourceLocation.tryParse(pentacleTag);
                if (pentacleResource != null)
                    tooltipComponents.add(
                            Component.translatable("item.occultengineering.pentacle_schematic.tooltip_prefix")
                                    .append(Component.translatable("multiblock." + pentacleResource.getNamespace() + "." + pentacleResource.getPath())
                                            .withStyle(ChatFormatting.RED)));
            }
        }
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
    }

    private static PentaclePrinter printer;

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        var stack = player.getItemInHand(usedHand);
        if (usedHand != InteractionHand.MAIN_HAND)
            return InteractionResultHolder.fail(stack);

        var tag = stack.getTag();
        if (tag == null) return InteractionResultHolder.fail(stack);
        if (!tag.contains("Pentacle")) return InteractionResultHolder.fail(stack);

        if (player.isShiftKeyDown() && player.onGround()) {
            tag.put("Position", NbtUtils.writeBlockPos(player.blockPosition()));
            tag.putBoolean("Deployed", true);
            player.displayClientMessage(
                    Component.literal("Set pentacle center position to ")
                            .append(player.blockPosition().toShortString())
                            .withStyle(ChatFormatting.GREEN),
                    true);
            stack.setTag(tag);
        }

        return InteractionResultHolder.success(stack);
    }
}
