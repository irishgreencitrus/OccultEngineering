package io.github.irishgreencitrus.occultengineering.content.block.phlogiport;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringParticleTypes;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.gameevent.EntityPositionSource;
import net.minecraft.world.level.gameevent.PositionSource;

public record PhlogiportSignalParticleData(PositionSource destination, int arrivalInTicks) implements ParticleOptions {
    private static final Codec<PositionSource> SAFE_POSITION_SOURCE_CODEC =
            PositionSource.CODEC.validate((p) -> p instanceof EntityPositionSource ? DataResult.error(() -> "Entity position sources are not allowed.") : DataResult.success(p));

    public static final MapCodec<PhlogiportSignalParticleData> CODEC =
            RecordCodecBuilder.mapCodec(
                    (i) -> i.group(
                                    SAFE_POSITION_SOURCE_CODEC.fieldOf("destination").forGetter(PhlogiportSignalParticleData::destination),
                                    Codec.INT.fieldOf("arrival_in_ticks").forGetter(PhlogiportSignalParticleData::arrivalInTicks))
                            .apply(i, PhlogiportSignalParticleData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, PhlogiportSignalParticleData> STREAM_CODEC =
            StreamCodec.composite(PositionSource.STREAM_CODEC,
                    PhlogiportSignalParticleData::destination,
                    ByteBufCodecs.VAR_INT,
                    PhlogiportSignalParticleData::arrivalInTicks,
                    PhlogiportSignalParticleData::new);

    @Override
    public ParticleType<PhlogiportSignalParticleData> getType() {
        return OccultEngineeringParticleTypes.PHLOGIPORT_SIGNAL.get();
    }

    public static ParticleType<PhlogiportSignalParticleData> createType() {
        return new ParticleType<>(false) {
            @Override
            public MapCodec<PhlogiportSignalParticleData> codec() {
                return CODEC;
            }

            @Override
            public StreamCodec<RegistryFriendlyByteBuf, PhlogiportSignalParticleData> streamCodec() {
                return STREAM_CODEC;
            }
        };
    }
}
