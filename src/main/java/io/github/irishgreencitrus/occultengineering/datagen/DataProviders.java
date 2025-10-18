package io.github.irishgreencitrus.occultengineering.datagen;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.simibubi.create.foundation.utility.FilesHelper;
import com.tterrag.registrate.providers.ProviderType;
import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import io.github.irishgreencitrus.occultengineering.content.ponder.OccultEngineeringPonderPlugin;
import io.github.irishgreencitrus.occultengineering.datagen.recipe.*;
import net.createmod.ponder.foundation.PonderIndex;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.nio.charset.StandardCharsets;
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

        if (event.includeServer()) {
            OcEngRecipeProvider.registerAllProcessing(generator, output, registries);
        }
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
        OccultEngineering.REGISTRATE.addDataGenerator(ProviderType.LANG, provider -> {
            BiConsumer<String, String> langConsumer = provider::add;

            provideDefaultLang("interface", langConsumer);
            provideDefaultLang("tooltips", langConsumer);
            providerPonderLang(langConsumer);
        });
    }
}
