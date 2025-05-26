package io.github.irishgreencitrus.occultengineering.mixin.accessor;

import com.klikli_dev.modonomicon.multiblock.matcher.TagMatcher;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.function.Supplier;

@Mixin(TagMatcher.class)
public interface TagMatcherAccessor {
    @Accessor(remap = false)
    Supplier<TagKey<Block>> getTag();
}
