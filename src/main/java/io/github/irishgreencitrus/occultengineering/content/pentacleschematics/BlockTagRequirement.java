package io.github.irishgreencitrus.occultengineering.content.pentacleschematics;

import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class BlockTagRequirement extends ItemRequirement.StackRequirement {
    TagKey<Block> tagKey;

    public BlockTagRequirement(ItemStack stack, ItemRequirement.ItemUseType usage, TagKey<Block> tag) {
        super(stack, usage);
        tagKey = tag;
    }

    @Override
    public boolean matches(ItemStack other) {
        if (!(stack.getItem() instanceof BlockItem blockItem)) return false;
        Block block = blockItem.getBlock();
        return block.defaultBlockState().is(tagKey);
    }
}
