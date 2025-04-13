package io.github.irishgreencitrus.occultengineering.datagen.book;

import com.klikli_dev.modonomicon.api.datagen.AbstractModonomiconLanguageProvider;
import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class EnUsProvider extends AbstractModonomiconLanguageProvider {
    public EnUsProvider(PackOutput output) {
        super(output, OccultEngineering.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {

    }

    @NotNull
    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        this.addTranslations();

        OccultEngineering.LOGGER.debug("Checking for book lang data {}", data());
        if (!this.data().isEmpty()) {
            OccultEngineering.LOGGER.debug("We have some book data!");
            data().forEach((key, value) -> {
                OccultEngineering.LOGGER.debug("Adding {} to REGISTRATE", key);
                OccultEngineering.REGISTRATE.addRawLang(key, value);
            });
        }

        return CompletableFuture.allOf();
    }
}
