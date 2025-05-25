package io.github.irishgreencitrus.occultengineering;

import io.github.irishgreencitrus.occultengineering.config.OccultEngineeringConfig;
import io.github.irishgreencitrus.occultengineering.content.ponder.OccultEngineeringPonderPlugin;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringPartialModels;
import net.createmod.catnip.config.ui.BaseConfigScreen;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class OccultEngineeringClient {

    public static void onCtorClient(IEventBus modEventBus) {
        OccultEngineeringPartialModels.register();

        modEventBus.addListener(OccultEngineeringClient::setup);
        OccultEngineering.LOGGER.info("Client setup is complete.");
    }

    public static void setup(final FMLClientSetupEvent event) {
        //OccultEngineeringPonderTags.register();
        BaseConfigScreen.setDefaultActionFor(OccultEngineering.MODID, base -> base
                .withButtonLabels("Client Settings", null, "Server Settings")
                .withSpecs(OccultEngineeringConfig.client().specification, null, OccultEngineeringConfig.server().specification)
        );
        PonderIndex.addPlugin(new OccultEngineeringPonderPlugin());
    }
}
