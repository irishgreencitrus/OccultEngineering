package io.github.irishgreencitrus.occultengineering.compat;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.level.LevelEvent;

public abstract class ModIntegration {
    Mods type;
    public ModIntegration(Mods thisType) {
        this.type = thisType;
    }
    public abstract void onCommonSetup(IEventBus modEventBus);
    public abstract void onClientSetup();
    public abstract void onWorldLoad(LevelEvent.Load event);
    public abstract void onWorldUnload(LevelEvent.Unload event);
    boolean isEnabled() {
        return type.isLoaded();
    }
}
