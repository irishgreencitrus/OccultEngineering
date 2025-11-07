package io.github.irishgreencitrus.occultengineering.event;

import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import io.github.irishgreencitrus.occultengineering.command.OcEngCommands;
import io.github.irishgreencitrus.occultengineering.compat.Mods;
import io.github.irishgreencitrus.occultengineering.compat.enchant_industry.OcEngEnchantIndustry;
import io.github.irishgreencitrus.occultengineering.content.block.mechanical_chamber.MechanicalChamberBlockEntity;
import io.github.irishgreencitrus.occultengineering.content.block.mechanical_pulverizer.PulverizerBlockEntity;
import io.github.irishgreencitrus.occultengineering.content.block.phlogiport.PhlogiportBlockEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.level.LevelEvent;

@EventBusSubscriber
public class OcEngEvents {
    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        OcEngCommands.register(event.getDispatcher(), event.getBuildContext());
    }

    @SubscribeEvent
    public static void onLoadWorld(LevelEvent.Load event) {
        Mods.LOADED_INTEGRATIONS.forEach(it -> it.onWorldLoad(event));
        var world = event.getLevel();
        OccultEngineering.PHLOGIPORT_NETWORK.onLoadWorld(world);
    }

    @SubscribeEvent
    public static void onUnloadWorld(LevelEvent.Unload event) {
        Mods.LOADED_INTEGRATIONS.forEach(it -> it.onWorldUnload(event));
        var world = event.getLevel();
        OccultEngineering.PHLOGIPORT_NETWORK.onUnloadWorld(world);
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        // TODO: do the rest of these
        PulverizerBlockEntity.registerCapabilities(event);
        PhlogiportBlockEntity.registerCapabilities(event);
        MechanicalChamberBlockEntity.registerCapabilities(event);
    }
}
