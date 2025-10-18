package io.github.irishgreencitrus.occultengineering.content.pentacleschematics.packet;

import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import io.github.irishgreencitrus.occultengineering.content.block.pucalith.PucalithBlockEntity;
import io.github.irishgreencitrus.occultengineering.content.block.pucalith.PucalithMenu;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringPackets;
import io.netty.buffer.ByteBuf;
import net.createmod.catnip.codecs.stream.CatnipStreamCodecBuilders;
import net.createmod.catnip.net.base.ServerboundPacketPayload;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;

public record PucalithSendOptionPacket(Option option) implements ServerboundPacketPayload {
    public static StreamCodec<ByteBuf, Option> OPTION_CODEC = CatnipStreamCodecBuilders.ofEnum(Option.class);

    public static final StreamCodec<ByteBuf, PucalithSendOptionPacket> STREAM_CODEC = StreamCodec.composite(
            OPTION_CODEC, PucalithSendOptionPacket::option,
            PucalithSendOptionPacket::new
    );

    public enum Option {
        PLAY,
        PAUSE,
        STOP
    }

    @Override
    public PacketTypeProvider getTypeProvider() {
        return OccultEngineeringPackets.PUCALITH_SEND_OPTION;
    }


    @Override
    public void handle(ServerPlayer player) {
        player.server.execute(() -> {
            if (!(player.containerMenu instanceof PucalithMenu))
                return;
            PucalithBlockEntity be = ((PucalithMenu) player.containerMenu).contentHolder;
            switch (option) {
                case PLAY -> be.onPlayButton();
                case PAUSE -> be.onPauseButton();
                case STOP -> be.onStopButton();
                default -> OccultEngineering.LOGGER.warn("Got an unexpected Pucalith option of {}", option);
            }
            be.sendUpdate = true;
        });
    }
}
