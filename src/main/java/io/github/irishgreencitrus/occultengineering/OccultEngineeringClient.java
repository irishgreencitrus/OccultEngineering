package io.github.irishgreencitrus.occultengineering;

import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringPartialModels;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@Mod(value = OccultEngineering.MODID, dist = Dist.CLIENT)
public class OccultEngineeringClient {
    public OccultEngineeringClient(IEventBus modEventBus) {
        OccultEngineeringPartialModels.init();

        modEventBus.addListener(OccultEngineeringClient::setup);
        OccultEngineering.LOGGER.info("Client setup is complete.");
    }


    public static void setup(final FMLClientSetupEvent event) {
        //OccultEngineeringPonderTags.register();
    }
}
