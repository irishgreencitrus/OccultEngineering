package io.github.irishgreencitrus.occultengineering.content.block.otherworld_detector.packet;

import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringPackets;
import io.netty.buffer.ByteBuf;
import net.createmod.catnip.net.base.ServerboundPacketPayload;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

// Occultism doesn't do this, and I don't know why. Let's fix that.
public record ThirdEyeActivationPacket(boolean isActive) implements ServerboundPacketPayload {
    public static final String DATA_ID = "occultengineering:third_eye_active";
    public static final StreamCodec<ByteBuf, ThirdEyeActivationPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, ThirdEyeActivationPacket::isActive,
            ThirdEyeActivationPacket::new
    );

    @Override
    public void handle(ServerPlayer player) {
        player.server.execute(() -> {
            player.getPersistentData().putBoolean(DATA_ID, isActive);
        });
    }

    public static boolean isActive(Player player) {
        return player.getPersistentData().getBoolean(DATA_ID);
    }

    @Override
    public PacketTypeProvider getTypeProvider() {
        return OccultEngineeringPackets.THIRD_EYE_ACTIVATION;
    }
}
