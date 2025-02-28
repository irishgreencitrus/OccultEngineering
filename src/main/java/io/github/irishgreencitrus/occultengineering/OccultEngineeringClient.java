package io.github.irishgreencitrus.occultengineering;

import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringPartialModels;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringPonderTags;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class OccultEngineeringClient {

    public static void onCtorClient(IEventBus modEventBus) {
        OccultEngineeringPartialModels.register();

        modEventBus.addListener(OccultEngineeringClient::setup);
        OccultEngineering.LOGGER.info("Client setup is complete.");
    }

    public static void setup(final FMLClientSetupEvent event) {
        OccultEngineeringPonderTags.register();
    }
}
