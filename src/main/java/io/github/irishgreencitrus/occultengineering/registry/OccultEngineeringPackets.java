package io.github.irishgreencitrus.occultengineering.registry;

import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import io.github.irishgreencitrus.occultengineering.content.block.otherworld_detector.packet.ThirdEyeActivationPacket;
import io.github.irishgreencitrus.occultengineering.content.block.phlogiport.packet.PhlogiportSendEffectPacket;
import io.github.irishgreencitrus.occultengineering.content.item.combinedgoggles.ToggleCombinedGogglesPacket;
import io.github.irishgreencitrus.occultengineering.content.pentacleschematics.packet.PentacleAltarConfirmPacket;
import io.github.irishgreencitrus.occultengineering.content.pentacleschematics.packet.PucalithSendOptionPacket;
import net.createmod.catnip.net.base.BasePacketPayload;
import net.createmod.catnip.net.base.CatnipPacketRegistry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.Locale;

public enum OccultEngineeringPackets implements BasePacketPayload.PacketTypeProvider {
    // CLIENT TO SERVER
    PENTACLE_ALTAR_CONFIRM(PentacleAltarConfirmPacket.class, PentacleAltarConfirmPacket.STREAM_CODEC),
    PUCALITH_SEND_OPTION(PucalithSendOptionPacket.class, PucalithSendOptionPacket.STREAM_CODEC),
    THIRD_EYE_ACTIVATION(ThirdEyeActivationPacket.class, ThirdEyeActivationPacket.STREAM_CODEC),
    TOGGLE_COMBINED_GOGGLES(ToggleCombinedGogglesPacket.class, ToggleCombinedGogglesPacket.STREAM_CODEC),

    // SERVER TO CLIENT
    PHLOGIPORT_SEND_EFFECT(PhlogiportSendEffectPacket.class, PhlogiportSendEffectPacket.STREAM_CODEC);

    private final CatnipPacketRegistry.PacketType<?> type;

    <T extends BasePacketPayload> OccultEngineeringPackets(Class<T> clazz, StreamCodec<? super RegistryFriendlyByteBuf, T> codec) {
        var name = this.name().toLowerCase(Locale.ROOT);
        this.type = new CatnipPacketRegistry.PacketType<>(
                new CustomPacketPayload.Type<>(OccultEngineering.asResource(name)),
                clazz, codec
        );
    }


    @SuppressWarnings("unchecked")
    @Override
    public <T extends CustomPacketPayload> CustomPacketPayload.Type<T> getType() {
        return (CustomPacketPayload.Type<T>) this.type.type();
    }

    public static void register() {
        var packetRegistry = new CatnipPacketRegistry(OccultEngineering.MODID, "1");
        for (OccultEngineeringPackets packet: OccultEngineeringPackets.values())
            packetRegistry.registerPacket(packet.type);
        packetRegistry.registerAllPackets();
    }
}
