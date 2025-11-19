package io.github.irishgreencitrus.occultengineering.registry;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class OccultEngineeringKeybinds {
    public static final Lazy<KeyMapping> TOGGLE_COMBINED_GOGGLES = Lazy.of(() ->
            new KeyMapping("key.occultengineering.toggle_combined_goggles",
                    KeyConflictContext.IN_GAME,
                    InputConstants.Type.KEYSYM.getOrCreate(GLFW.GLFW_KEY_UNKNOWN),
                    "key.categories.occultengineering.keys"));

    @SubscribeEvent
    public static void registerKeybinds(RegisterKeyMappingsEvent event) {
        event.register(TOGGLE_COMBINED_GOGGLES.get());
    }
}
