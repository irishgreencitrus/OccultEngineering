package io.github.irishgreencitrus.occultengineering.event;

import io.github.irishgreencitrus.occultengineering.command.OcEngCommands;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class OcEngEvents {
    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        OcEngCommands.register(event.getDispatcher());
    }
}
