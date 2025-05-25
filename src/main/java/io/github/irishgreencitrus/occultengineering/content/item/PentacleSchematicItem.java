package io.github.irishgreencitrus.occultengineering.content.item;

import com.klikli_dev.modonomicon.data.MultiblockDataManager;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringItems;
import net.createmod.catnip.nbt.NBTHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;

public class PentacleSchematicItem extends Item {
    public PentacleSchematicItem(Properties properties) {
        super(properties);
    }

    public static ItemStack create(ResourceLocation pentacleLocation, String owner) {
        var blueprint = OccultEngineeringItems.PENTACLE_SCHEMATIC.asStack();

        CompoundTag tag = new CompoundTag();
        tag.putBoolean("Deployed", false);
        tag.putString("Owner", owner);
        tag.putString("Pentacle", pentacleLocation.toString());
        tag.put("Anchor", NbtUtils.writeBlockPos(BlockPos.ZERO));
        tag.putString("Rotation", Rotation.NONE.name());
        tag.putString("Mirror", Mirror.NONE.name());
        tag.put("Bounds", NBTHelper.writeVec3i(MultiblockDataManager.get().getMultiblock(pentacleLocation).getSize()));

        blueprint.setTag(tag);
        return blueprint;
    }
}
