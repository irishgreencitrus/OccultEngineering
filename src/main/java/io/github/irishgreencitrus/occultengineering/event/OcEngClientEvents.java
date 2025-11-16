package io.github.irishgreencitrus.occultengineering.event;

import com.klikli_dev.occultism.Occultism;
import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import io.github.irishgreencitrus.occultengineering.content.block.otherworld_detector.packet.ThirdEyeActivationPacket;
import io.github.irishgreencitrus.occultengineering.content.item.combinedgoggles.ToggleCombinedGogglesPacket;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringKeybinds;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.event.level.LevelEvent;

@EventBusSubscriber(Dist.CLIENT)
public class OcEngClientEvents {
    private static boolean lastState = false;

    @SubscribeEvent
    public static void onClientWorldLoad(LevelEvent.Load event) {
        if (event.getLevel().isClientSide()) {
            Minecraft.getInstance().tell(() -> {
                        boolean currentState = Occultism.THIRD_EYE_EFFECT_RENDERER.gogglesActiveLastTick
                                || Occultism.THIRD_EYE_EFFECT_RENDERER.thirdEyeActiveLastTick;
                        lastState = currentState;
                        CatnipServices.NETWORK.sendToServer(new ThirdEyeActivationPacket(currentState));
                    }
            );
        }
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        while (OccultEngineeringKeybinds.TOGGLE_COMBINED_GOGGLES.get().consumeClick()) {
            OccultEngineering.LOGGER.info("Toggled combined goggles");
            CatnipServices.NETWORK.sendToServer(new ToggleCombinedGogglesPacket());
        }

        boolean currentState = Occultism.THIRD_EYE_EFFECT_RENDERER.gogglesActiveLastTick
                || Occultism.THIRD_EYE_EFFECT_RENDERER.thirdEyeActiveLastTick;
        if (currentState != lastState) {
            lastState = currentState;
            CatnipServices.NETWORK.sendToServer(new ThirdEyeActivationPacket(currentState));
        }
    }
}
