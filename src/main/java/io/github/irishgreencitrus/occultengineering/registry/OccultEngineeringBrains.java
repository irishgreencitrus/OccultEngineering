package io.github.irishgreencitrus.occultengineering.registry;

import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import io.github.irishgreencitrus.occultengineering.content.entity.brain.DynamicBrainFactory;
import io.github.irishgreencitrus.occultengineering.content.entity.puca.PucaEntity;
import io.github.irishgreencitrus.occultengineering.content.entity.puca.brain.PucaEmptyBrain;
import io.github.irishgreencitrus.occultengineering.content.entity.puca.brain.PucaWanderBrain;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class OccultEngineeringBrains {
    public static DeferredRegister<DynamicBrainFactory<?>> BRAINS = DeferredRegister.create(OccultEngineering.asResource("brain_factory"), OccultEngineering.MODID);

    public static final Supplier<IForgeRegistry<DynamicBrainFactory<?>>> REGISTRY = BRAINS.makeRegistry(() ->
            new RegistryBuilder<DynamicBrainFactory<?>>().disableSaving().setMaxID(Integer.MAX_VALUE - 1));

    public static final RegistryObject<DynamicBrainFactory<PucaEntity>> PUCA_EMPTY = BRAINS.register("puca_empty",
            () -> new DynamicBrainFactory<>(PucaEmptyBrain::new, PucaEntity.class));

    public static final RegistryObject<DynamicBrainFactory<PucaEntity>> PUCA_WANDER = BRAINS.register("puca_wander",
            () -> new DynamicBrainFactory<>(PucaWanderBrain::new, PucaEntity.class));

    public static void register(IEventBus modEventBus) {
        BRAINS.register(modEventBus);
    }
}
