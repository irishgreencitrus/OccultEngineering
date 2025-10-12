package io.github.irishgreencitrus.occultengineering.datagen;

import io.github.irishgreencitrus.occultengineering.datagen.book.EnUsProvider;
import io.github.irishgreencitrus.occultengineering.datagen.book.OcEngBookProvider;
import net.minecraft.data.DataGenerator;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber
public class BookProvider {

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        generator.addProvider(event.includeServer(), new PentacleProvider(generator));

        var bookLang = new EnUsProvider(generator.getPackOutput());
        generator.addProvider(event.includeServer(), new OcEngBookProvider(generator.getPackOutput(), bookLang));
        generator.addProvider(event.includeClient(), bookLang);
    }
}
