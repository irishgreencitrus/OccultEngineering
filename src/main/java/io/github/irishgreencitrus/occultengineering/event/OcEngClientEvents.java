package io.github.irishgreencitrus.occultengineering.event;

import com.klikli_dev.occultism.Occultism;
import io.github.irishgreencitrus.occultengineering.content.block.otherworld_detector.packet.ThirdEyeActivationPacket;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringPackets;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class OcEngClientEvents {
    private static boolean lastState = false;

    @SubscribeEvent
    public static void onClientWorldLoad(LevelEvent.Load event) {
        if (event.getLevel().isClientSide()) {
            Minecraft.getInstance().tell(() -> {
                        boolean currentState = Occultism.THIRD_EYE_EFFECT_RENDERER.gogglesActiveLastTick
                                || Occultism.THIRD_EYE_EFFECT_RENDERER.thirdEyeActiveLastTick;
                        lastState = currentState;
                        OccultEngineeringPackets.getChannel().sendToServer(new ThirdEyeActivationPacket(currentState));
                    }
            );
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        boolean currentState = Occultism.THIRD_EYE_EFFECT_RENDERER.gogglesActiveLastTick
                || Occultism.THIRD_EYE_EFFECT_RENDERER.thirdEyeActiveLastTick;
        if (currentState != lastState) {
            lastState = currentState;
            OccultEngineeringPackets.getChannel().sendToServer(new ThirdEyeActivationPacket(currentState));
        }
    }
}
