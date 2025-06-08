package io.github.irishgreencitrus.occultengineering.content.pentacleschematics;

import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class BlockTagRequirement extends ItemRequirement.StackRequirement {
    TagKey<Block> tagKey;

    public BlockTagRequirement(ItemStack stack, TagKey<Block> tag) {
        // Since we're using block tags, the Item has to be consumed.
        super(stack, ItemRequirement.ItemUseType.CONSUME);
        tagKey = tag;
    }

    @Override
    public boolean matches(ItemStack other) {
        if (!(stack.getItem() instanceof BlockItem blockItem)) return false;
        Block block = blockItem.getBlock();
        return block.defaultBlockState().is(tagKey);
    }

    public boolean matches(TagKey<Block> blockTag) {
        return tagKey.equals(blockTag);
    }
}
