package io.github.irishgreencitrus.occultengineering.event;

import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import io.github.irishgreencitrus.occultengineering.command.OcEngCommands;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class OcEngEvents {
    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        OcEngCommands.register(event.getDispatcher());
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) return;
        if (OccultEngineering.CURRENT_PENTACLE_PRINTER != null) {
            OccultEngineering.CURRENT_PENTACLE_PRINTER.serverTick();
        }
    }
}
