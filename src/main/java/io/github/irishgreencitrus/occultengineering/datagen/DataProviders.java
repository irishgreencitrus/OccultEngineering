package io.github.irishgreencitrus.occultengineering.datagen;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.simibubi.create.foundation.utility.FilesHelper;
import com.tterrag.registrate.providers.ProviderType;
import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import io.github.irishgreencitrus.occultengineering.content.ponder.OccultEngineeringPonderPlugin;
import io.github.irishgreencitrus.occultengineering.datagen.recipe.*;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.function.BiConsumer;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataProviders {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        var generator = event.getGenerator();
        var output = generator.getPackOutput();

        generator.addProvider(event.includeServer(), new OcEngCompactingRecipeGen(output));
        generator.addProvider(event.includeServer(), new OcEngMixingRecipeGen(output));
        generator.addProvider(event.includeServer(), new OcEngFillingRecipeGen(output));
        generator.addProvider(event.includeServer(), new OcEngItemApplicationRecipeGen(output));
        generator.addProvider(event.includeServer(), new OcEngStandardRecipeGen(output));

        OccultEngineering.REGISTRATE.addDataGenerator(ProviderType.LANG, provider -> {
            BiConsumer<String, String> langConsumer = provider::add;

            provideDefaultLang("interface", langConsumer);
            provideDefaultLang("tooltips", langConsumer);
            providerPonderLang(langConsumer);
        });
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
}
