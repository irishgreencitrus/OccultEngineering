package io.github.irishgreencitrus.occultengineering.registry;

import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import io.github.irishgreencitrus.occultengineering.content.entity.brain.DynamicBrainFactory;
import io.github.irishgreencitrus.occultengineering.content.entity.puca.PucaEntity;
import io.github.irishgreencitrus.occultengineering.content.entity.puca.brain.PucaConstructionBrain;
import io.github.irishgreencitrus.occultengineering.content.entity.puca.brain.PucaEmptyBrain;
import io.github.irishgreencitrus.occultengineering.content.entity.puca.brain.PucaWanderBrain;
import net.minecraft.core.Registry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class OccultEngineeringBrains {
    public static DeferredRegister<DynamicBrainFactory<?>> BRAINS = DeferredRegister.create(OccultEngineering.asResource("brain_factory"), OccultEngineering.MODID);

    public static final Registry<DynamicBrainFactory<?>> REGISTRY = BRAINS.makeRegistry((b) -> {});

    public static final DeferredHolder<DynamicBrainFactory<?>, DynamicBrainFactory<PucaEntity>> PUCA_EMPTY = BRAINS.register("puca_empty",
            () -> new DynamicBrainFactory<>(PucaEmptyBrain::new, PucaEntity.class));

    public static final DeferredHolder<DynamicBrainFactory<?>, DynamicBrainFactory<PucaEntity>> PUCA_CONSTRUCT = BRAINS.register("puca_construct",
            () -> new DynamicBrainFactory<>(PucaConstructionBrain::new, PucaEntity.class));

    public static final DeferredHolder<DynamicBrainFactory<?>, DynamicBrainFactory<PucaEntity>> PUCA_WANDER = BRAINS.register("puca_wander",
            () -> new DynamicBrainFactory<>(PucaWanderBrain::new, PucaEntity.class));

    public static void register(IEventBus modEventBus) {
        BRAINS.register(modEventBus);
    }
}
