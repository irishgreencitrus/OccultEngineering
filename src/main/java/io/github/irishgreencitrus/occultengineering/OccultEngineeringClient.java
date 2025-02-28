package io.github.irishgreencitrus.occultengineering;

import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringPartialModels;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringPonderTags;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class OccultEngineeringClient {
    public OccultEngineeringClient(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        OccultEngineeringPartialModels.register();

        modEventBus.register(this);
        OccultEngineering.LOGGER.info("Client setup is complete.");
    }

    @SubscribeEvent
    public void setup(final FMLClientSetupEvent event) {
        OccultEngineeringPonderTags.register();
    }
}
