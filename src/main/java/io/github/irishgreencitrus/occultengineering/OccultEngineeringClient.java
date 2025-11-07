package io.github.irishgreencitrus.occultengineering;

import io.github.irishgreencitrus.occultengineering.compat.ModIntegration;
import io.github.irishgreencitrus.occultengineering.compat.Mods;
import io.github.irishgreencitrus.occultengineering.config.OccultEngineeringConfig;
import io.github.irishgreencitrus.occultengineering.content.ponder.OccultEngineeringPonderPlugin;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringEntities;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringPartialModels;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringParticleTypes;
import net.createmod.catnip.config.ui.BaseConfigScreen;
import net.createmod.ponder.foundation.PonderIndex;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@Mod(value = OccultEngineering.MODID, dist = Dist.CLIENT)
public class OccultEngineeringClient {
    public OccultEngineeringClient(IEventBus modEventBus) {
        onCtorClient(modEventBus);
    }

    public static void onCtorClient(IEventBus modEventBus) {
        OccultEngineeringPartialModels.register();

        modEventBus.addListener(OccultEngineeringClient::setup);
        modEventBus.addListener(OccultEngineeringEntities::clientRegisterRenderers);
        modEventBus.addListener(OccultEngineeringParticleTypes::registerProviders);
        Mods.LOADED_INTEGRATIONS.forEach(ModIntegration::onClientSetup);
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
