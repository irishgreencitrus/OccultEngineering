package io.github.irishgreencitrus.occultengineering.content.phlogiport;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.level.gameevent.PositionSourceType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Locale;

public class PhlogiportSignalParticleData implements ParticleOptions {
    /** Offset from the block centre to the top of the Phlogiport antenna. */
    public static final Vec3 ANTENNA_OFFSET = new Vec3(0, 13f / 32f, 0);

    public static final Codec<PhlogiportSignalParticleData> CODEC = RecordCodecBuilder.create(i ->
            i.group(
                    PositionSource.CODEC.fieldOf("destination").forGetter(p -> p.destination),
                    Codec.INT.fieldOf("lifetime").forGetter(p -> p.lifetime)
            ).apply(i, PhlogiportSignalParticleData::new)
    );

    public static final ParticleOptions.Deserializer<PhlogiportSignalParticleData> DESERIALIZER = new Deserializer<>() {
        @Override
        public PhlogiportSignalParticleData fromCommand(ParticleType<PhlogiportSignalParticleData> particleType, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            var x = reader.readDouble();
            reader.expect(' ');
            var y = reader.readDouble();
            reader.expect(' ');
            var z = reader.readDouble();
            reader.expect(' ');
            int l = reader.readInt();
            return new PhlogiportSignalParticleData(new BlockPositionSource(BlockPos.containing(x, y, z)), l);
        }

        @Override
        public PhlogiportSignalParticleData fromNetwork(ParticleType<PhlogiportSignalParticleData> particleType, FriendlyByteBuf b) {
            return new PhlogiportSignalParticleData(PositionSourceType.fromNetwork(b), b.readVarInt());
        }
    };

    private final PositionSource destination;
    private final int lifetime;

    public PhlogiportSignalParticleData(PositionSource destination, int lifetime) {
        this.destination = destination;
        this.lifetime = lifetime;
    }

    @Override
    public ParticleType<?> getType() {
        return OccultEngineeringParticleTypes.PHLOGIPORT_SIGNAL.get();
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buffer) {
        PositionSourceType.toNetwork(this.destination, buffer);
        buffer.writeVarInt(this.lifetime);
    }

    @Override
    public String writeToString() {
        Vec3 vec3 = this.destination.getPosition((Level) null).get();
        double d0 = vec3.x();
        double d1 = vec3.y();
        double d2 = vec3.z();
        return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %d", ForgeRegistries.PARTICLE_TYPES.getKey(this.getType()), d0, d1, d2, this.lifetime);
    }

    public int getLifetime() {
        return lifetime;
    }

    public PositionSource getDestination() {
        return destination;
    }
}
