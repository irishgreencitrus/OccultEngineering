package io.github.irishgreencitrus.occultengineering;

import io.github.irishgreencitrus.occultengineering.config.OccultEngineeringConfig;
import io.github.irishgreencitrus.occultengineering.content.ponder.OccultEngineeringPonderPlugin;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringEntities;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringPartialModels;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringParticleTypes;
import net.createmod.catnip.config.ui.BaseConfigScreen;
import net.createmod.ponder.foundation.PonderIndex;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

public class OccultEngineeringClient {

    public static void onCtorClient(IEventBus modEventBus) {
        OccultEngineeringPartialModels.register();

        modEventBus.addListener(OccultEngineeringClient::setup);
        modEventBus.addListener(OccultEngineeringEntities::clientRegisterRenderers);
        modEventBus.addListener(OccultEngineeringParticleTypes::registerProviders);
        OccultEngineering.LOGGER.info("Client setup is complete.");
    }

    public static void setup(final FMLClientSetupEvent event) {
        BaseConfigScreen.setDefaultActionFor(OccultEngineering.MODID, base -> base
                .withButtonLabels("Client Settings", null, "Server Settings")
                .withSpecs(OccultEngineeringConfig.client().specification, null, OccultEngineeringConfig.server().specification)
        );
        PonderIndex.addPlugin(new OccultEngineeringPonderPlugin());
    }
}
