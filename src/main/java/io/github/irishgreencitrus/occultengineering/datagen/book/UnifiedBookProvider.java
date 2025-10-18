package io.github.irishgreencitrus.occultengineering.datagen.book;

import com.klikli_dev.modonomicon.api.datagen.BookProvider;
import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;

import java.util.concurrent.CompletableFuture;

public class UnifiedBookProvider implements DataProvider {
    BookProvider bookProvider;
    EnUsProvider enUsProvider;
    public UnifiedBookProvider(BookProvider bookProvider, EnUsProvider enUsProvider) {
        this.bookProvider = bookProvider;
        this.enUsProvider = enUsProvider;

    }
    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput) {
        var bookFutures = bookProvider.run(cachedOutput);
        bookFutures.join();

        OccultEngineering.LOGGER.info("Book has {} lang entries",enUsProvider.data().size());

        enUsProvider.run(cachedOutput);
        return CompletableFuture.allOf();
    }

    @Override
    public String getName() {
        return "Occult Engineering - Unified Book Provider";
    }
}
