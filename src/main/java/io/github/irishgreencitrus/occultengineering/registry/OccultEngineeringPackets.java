package io.github.irishgreencitrus.occultengineering.registry;

import com.simibubi.create.foundation.networking.SimplePacketBase;
import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import io.github.irishgreencitrus.occultengineering.content.pentacleschematics.packet.PentacleAltarConfirmPacket;
import io.github.irishgreencitrus.occultengineering.content.pentacleschematics.packet.PucalithSendOptionPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static net.minecraftforge.network.NetworkDirection.PLAY_TO_SERVER;

public enum OccultEngineeringPackets {
    // CLIENT TO SERVER
    PENTACLE_ALTAR_CONFIRM(PentacleAltarConfirmPacket.class, PentacleAltarConfirmPacket::new, PLAY_TO_SERVER),
    PUCALITH_SEND_OPTION(PucalithSendOptionPacket.class, PucalithSendOptionPacket::new, PLAY_TO_SERVER);

    public static final ResourceLocation CHANNEL_NAME = OccultEngineering.asResource("main");
    public static final int NETWORK_VERSION = 1;
    public static final String NETWORK_VERSION_STR = String.valueOf(NETWORK_VERSION);
    private final PacketType<?> packetType;

    <T extends SimplePacketBase> OccultEngineeringPackets(Class<T> type, Function<FriendlyByteBuf, T> factory, NetworkDirection networkDirection) {
        packetType = new PacketType<>(type, factory, networkDirection);
    }


    private static SimpleChannel channel;

    public static void registerPackets() {
        channel = NetworkRegistry.ChannelBuilder
                .named(CHANNEL_NAME)
                .serverAcceptedVersions(NETWORK_VERSION_STR::equals)
                .clientAcceptedVersions(NETWORK_VERSION_STR::equals)
                .networkProtocolVersion(() -> NETWORK_VERSION_STR)
                .simpleChannel();

        for (var packet : values())
            packet.packetType.register();

    }

    public static SimpleChannel getChannel() {
        return channel;
    }


    private static class PacketType<T extends SimplePacketBase> {
        private static int index = 0;

        private BiConsumer<T, FriendlyByteBuf> encoder;
        private Function<FriendlyByteBuf, T> decoder;
        private BiConsumer<T, Supplier<NetworkEvent.Context>> handler;
        private Class<T> type;
        private NetworkDirection direction;

        private PacketType(Class<T> type, Function<FriendlyByteBuf, T> factory, NetworkDirection direction) {
            encoder = T::write;
            decoder = factory;
            handler = (packet, contextSupplier) -> {
                NetworkEvent.Context context = contextSupplier.get();
                if (packet.handle(context)) {
                    context.setPacketHandled(true);
                }
            };
            this.type = type;
            this.direction = direction;
        }

        private void register() {
            getChannel().messageBuilder(type, index++, direction)
                    .encoder(encoder)
                    .decoder(decoder)
                    .consumerNetworkThread(handler)
                    .add();
        }
    }
}
