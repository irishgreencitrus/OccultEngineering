package io.github.irishgreencitrus.occultengineering.datagen;

import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import io.github.irishgreencitrus.occultengineering.datagen.book.EnUsProvider;
import io.github.irishgreencitrus.occultengineering.datagen.book.OcEngBookProvider;
import net.minecraft.data.DataGenerator;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Collections;

@EventBusSubscriber
public class BookProvider {

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        generator.addProvider(event.includeServer(), new PentacleProvider(generator));

        var bookLang = new EnUsProvider(generator.getPackOutput());
        generator.addProvider(event.includeServer(),
                new com.klikli_dev.modonomicon.api.datagen.BookProvider(generator.getPackOutput(),
                        event.getLookupProvider(),
                        OccultEngineering.MODID,
                        Collections.singletonList(new OcEngBookProvider(bookLang))));
        generator.addProvider(event.includeClient(), bookLang);
    }
}
