package io.github.irishgreencitrus.occultengineering.mixin.accessor;

import com.klikli_dev.occultism.common.ritual.SummonRitual;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SummonRitual.class)
public interface SummonRitualAccessor {
    @Accessor(remap = false)
    boolean getTame();
}
