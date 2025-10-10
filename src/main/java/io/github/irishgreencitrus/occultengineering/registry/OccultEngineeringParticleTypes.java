package io.github.irishgreencitrus.occultengineering.registry;

import com.mojang.serialization.Codec;
import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import io.github.irishgreencitrus.occultengineering.content.phlogiport.PhlogiportSignalParticle;
import io.github.irishgreencitrus.occultengineering.content.phlogiport.PhlogiportSignalParticleData;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class OccultEngineeringParticleTypes {
    private static final DeferredRegister<ParticleType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, OccultEngineering.MODID);

    public static RegistryObject<ParticleType<PhlogiportSignalParticleData>> PHLOGIPORT_SIGNAL =
            register("phlogiport_signal", PhlogiportSignalParticleData.DESERIALIZER, (p) -> PhlogiportSignalParticleData.CODEC);

    public static void register(IEventBus modEventBus) {
        REGISTRY.register(modEventBus);
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(PHLOGIPORT_SIGNAL.get(), PhlogiportSignalParticle.Provider::new);
    }

    private static <T extends ParticleOptions> RegistryObject<ParticleType<T>> register(String name, ParticleOptions.Deserializer<T> deserializer, Function<ParticleType<T>, Codec<T>> codecFactory) {
        return REGISTRY.register(name, () -> new ParticleType<>(false, deserializer) {
            public @NotNull Codec<T> codec() {
                return codecFactory.apply(this);
            }
        });
    }
}
