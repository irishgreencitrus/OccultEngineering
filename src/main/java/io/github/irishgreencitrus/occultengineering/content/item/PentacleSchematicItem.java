package io.github.irishgreencitrus.occultengineering.content.item;

import com.klikli_dev.modonomicon.data.MultiblockDataManager;
import com.simibubi.create.content.schematics.cannon.MaterialChecklist;
import io.github.irishgreencitrus.occultengineering.content.pentacleschematics.PentacleSchematic;
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


    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        var stack = player.getItemInHand(usedHand);
        if (usedHand != InteractionHand.MAIN_HAND)
            return InteractionResultHolder.fail(stack);
        else if (stack.hasTag() && stack.getTag().contains("Pentacle")) {
            var tag = stack.getTag();
            tag.put("Position", NbtUtils.writeBlockPos(player.blockPosition()));
            stack.setTag(tag);

            var schem = PentacleSchematic.fromStack(level, stack).get();
            var mats = new MaterialChecklist();
            mats.require(schem.getItemRequirement());
            var clipboard = mats.createWrittenClipboard();
            player.addItem(clipboard);
        }

        return super.use(level, player, usedHand);
    }
}
