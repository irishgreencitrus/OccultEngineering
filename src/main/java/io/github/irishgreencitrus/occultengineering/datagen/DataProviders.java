package io.github.irishgreencitrus.occultengineering.datagen;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.klikli_dev.modonomicon.api.datagen.BookProvider;
import com.simibubi.create.foundation.utility.FilesHelper;
import com.tterrag.registrate.providers.ProviderType;
import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import io.github.irishgreencitrus.occultengineering.content.ponder.OccultEngineeringPonderPlugin;
import io.github.irishgreencitrus.occultengineering.datagen.book.EnUsProvider;
import io.github.irishgreencitrus.occultengineering.datagen.book.OcEngBookProvider;
import io.github.irishgreencitrus.occultengineering.datagen.book.UnifiedBookProvider;
import io.github.irishgreencitrus.occultengineering.datagen.recipe.*;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.data.DataGenerator;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

@EventBusSubscriber
public class DataProviders {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        var generator = event.getGenerator();
        var output = generator.getPackOutput();
        var registries = event.getLookupProvider();
        generator.addProvider(event.includeServer(), new OcEngStandardRecipeGen(output, registries));
        createBook(event, generator);

        if (event.includeServer()) {
            OcEngRecipeProvider.registerAllProcessing(generator, output, registries);
        }
    }

    @Nullable
    static EnUsProvider bookLang = null;

    private static void createBook(GatherDataEvent event, DataGenerator generator) {
        generator.addProvider(event.includeServer(), new PentacleProvider(generator));
        bookLang = new EnUsProvider(generator.getPackOutput());
        var bookProvider = new BookProvider(generator.getPackOutput(),
                        event.getLookupProvider(),
                        OccultEngineering.MODID,
                        List.of(new OcEngBookProvider(bookLang)));

        var unifiedProvider = new UnifiedBookProvider(bookProvider, bookLang);
        generator.addProvider(event.includeServer() || event.includeClient(), unifiedProvider);
    }

    private static void provideDefaultLang(String fileName, BiConsumer<String, String> consumer) {
        String path = "assets/occultengineering/lang/default/" + fileName + ".json";
        JsonElement jsonElement = FilesHelper.loadJsonResource(path);
        if (jsonElement == null) {
            throw new IllegalStateException(String.format("Could not find default lang file: %s", path));
        }
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            String key = entry.getKey();
            String value = new String(entry.getValue().getAsString().getBytes(), StandardCharsets.UTF_8);
            consumer.accept(key, value);
        }
    }

    private static void providerPonderLang(BiConsumer<String, String> consumer) {
        PonderIndex.addPlugin(new OccultEngineeringPonderPlugin());
        PonderIndex.getLangAccess().provideLang(OccultEngineering.MODID, consumer);
    }

    public static void registerAdditionalLangProviders() {
        //OccultEngineering.LOGGER.info("Registering additional langs...");
        OccultEngineering.REGISTRATE.addDataGenerator(ProviderType.LANG, provider -> {
            BiConsumer<String, String> langConsumer = provider::add;

            provideDefaultLang("interface", langConsumer);
            provideDefaultLang("tooltips", langConsumer);
            providerPonderLang(langConsumer);
        });
    }
}
