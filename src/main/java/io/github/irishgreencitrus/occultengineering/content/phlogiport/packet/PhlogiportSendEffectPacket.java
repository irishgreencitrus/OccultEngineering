package io.github.irishgreencitrus.occultengineering.content.phlogiport.packet;

import io.github.irishgreencitrus.occultengineering.content.phlogiport.PhlogiportBlockEntity;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringPackets;
import io.netty.buffer.ByteBuf;
import net.createmod.catnip.net.base.ClientboundPacketPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public record PhlogiportSendEffectPacket(BlockPos position, boolean isReceiver, boolean success) implements ClientboundPacketPayload {
    public static final StreamCodec<ByteBuf, PhlogiportSendEffectPacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, PhlogiportSendEffectPacket::position,
            ByteBufCodecs.BOOL, PhlogiportSendEffectPacket::isReceiver,
            ByteBufCodecs.BOOL, PhlogiportSendEffectPacket::success,
            PhlogiportSendEffectPacket::new
    );

    @Override
    @OnlyIn(Dist.CLIENT)
    public void handle(LocalPlayer player) {
        var level = Minecraft.getInstance().level;
        if (level == null) return;
        if (level.getBlockEntity(position) instanceof PhlogiportBlockEntity pbe) {
            pbe.playEffect(isReceiver, success);

        }
    }

    @Override
    public PacketTypeProvider getTypeProvider() {
        return OccultEngineeringPackets.PHLOGIPORT_SEND_EFFECT;
    };
}
