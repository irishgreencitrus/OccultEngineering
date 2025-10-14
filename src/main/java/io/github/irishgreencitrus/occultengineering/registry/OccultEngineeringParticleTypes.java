package io.github.irishgreencitrus.occultengineering.registry;

import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import io.github.irishgreencitrus.occultengineering.content.block.phlogiport.PhlogiportSignalParticle;
import io.github.irishgreencitrus.occultengineering.content.block.phlogiport.PhlogiportSignalParticleData;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class OccultEngineeringParticleTypes {
    private static final DeferredRegister<ParticleType<?>> REGISTRY = DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, OccultEngineering.MODID);

    public static Supplier<ParticleType<PhlogiportSignalParticleData>> PHLOGIPORT_SIGNAL =
            REGISTRY.register("phlogiport_signal", PhlogiportSignalParticleData::createType);

    public static void register(IEventBus modEventBus) {
        REGISTRY.register(modEventBus);
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(PHLOGIPORT_SIGNAL.get(), PhlogiportSignalParticle.Provider::new);
    }
}
