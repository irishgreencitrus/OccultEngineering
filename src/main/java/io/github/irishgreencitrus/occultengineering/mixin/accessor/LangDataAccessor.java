package io.github.irishgreencitrus.occultengineering.mixin.accessor;

import com.klikli_dev.modonomicon.api.datagen.AbstractModonomiconLanguageProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(AbstractModonomiconLanguageProvider.class)
public interface LangDataAccessor {
    @Accessor(remap = false)
    Map<String, String> getData();
}
