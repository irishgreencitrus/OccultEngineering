package io.github.irishgreencitrus.occultengineering.datagen;

import io.github.irishgreencitrus.occultengineering.datagen.book.EnUsProvider;
import io.github.irishgreencitrus.occultengineering.datagen.book.OcEngBookProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
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
