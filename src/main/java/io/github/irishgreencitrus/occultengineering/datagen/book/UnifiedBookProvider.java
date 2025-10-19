package io.github.irishgreencitrus.occultengineering.datagen.book;

import com.klikli_dev.modonomicon.api.datagen.BookProvider;
import com.tterrag.registrate.providers.RegistrateGenericProvider;
import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class UnifiedBookProvider implements RegistrateGenericProvider.Generator, DataProvider {
    PackOutput output;
    CompletableFuture<HolderLookup.Provider> registries;
    public static Map<String, String> bookData;

    public UnifiedBookProvider(RegistrateGenericProvider.GeneratorData data) {
        this.output = data.output();
        this.registries = data.registries();
    }
    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput) {
        if (output == null || registries == null) throw new IllegalStateException("Must be generated after event");
        OccultEngineering.LOGGER.info("Running Unified Book Provider");
        var bookLang = new EnUsProvider(output);
        var bookProvider = new BookProvider(output,
                registries,
                OccultEngineering.MODID,
                List.of(new OcEngBookProvider(bookLang)));

        var bookFutures = bookProvider.run(cachedOutput);

        bookFutures.join();

        OccultEngineering.LOGGER.info("Book has {} lang entries", bookLang.data().size());
        bookData = bookLang.data();
        return CompletableFuture.allOf();
    }

    @Override
    public String getName() {
        return "Occult Engineering - Unified Book Provider";
    }

    @Override
    public DataProvider generate(RegistrateGenericProvider.GeneratorData data) {
        this.output = data.output();
        OccultEngineering.LOGGER.info("Output is {}", this.output);
        this.registries = data.registries();
        OccultEngineering.LOGGER.info("Registries is {}", this.registries);
        return this;
    }
}
