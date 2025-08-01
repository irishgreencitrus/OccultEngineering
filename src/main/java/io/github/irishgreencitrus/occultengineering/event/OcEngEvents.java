package io.github.irishgreencitrus.occultengineering.event;

import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import io.github.irishgreencitrus.occultengineering.command.OcEngCommands;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = OccultEngineering.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class OcEngEvents {
    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        OcEngCommands.register(event.getDispatcher(), event.getBuildContext());
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) return;
    }

    @SubscribeEvent
    public static void onLoadWorld(LevelEvent.Load event) {
        var world = event.getLevel();
        OccultEngineering.PHLOGIPORT_NETWORK.onLoadWorld(world);
    }

    @SubscribeEvent
    public static void onUnloadWorld(LevelEvent.Unload event) {
        var world = event.getLevel();
        OccultEngineering.PHLOGIPORT_NETWORK.onUnloadWorld(world);
    }
}
