package io.github.irishgreencitrus.occultengineering.datagen.book;

import com.klikli_dev.modonomicon.api.datagen.AbstractModonomiconLanguageProvider;
import com.tterrag.registrate.providers.RegistrateLangProvider;
import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

/*
    Looking for language data?
    You'll find all the language data for the book in the respective category classes.
 */
public class EnUsProvider extends AbstractModonomiconLanguageProvider {
    public EnUsProvider(PackOutput output) {
        super(output, OccultEngineering.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
    }

    @Override
    public void accept(String key, String value) {
        super.accept(key, value);
        OccultEngineering.LOGGER.debug("Accepting {} -> {}",key,value);
    }

    @NotNull
    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        this.addTranslations();

        if (!this.data().isEmpty()) {
            data().forEach((key, value) -> {
                OccultEngineering.LOGGER.debug("Adding {} to REGISTRATE", key);
                OccultEngineering.REGISTRATE.addRawLang(key, value);
            });
        } else {
            OccultEngineering.LOGGER.warn("No data available to consume");
        }

        return CompletableFuture.allOf();
    }
}
