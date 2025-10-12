package io.github.irishgreencitrus.occultengineering.registry;

import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import io.github.irishgreencitrus.occultengineering.content.entity.puca.PucaEntity;
import io.github.irishgreencitrus.occultengineering.content.entity.puca.PucaRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class OccultEngineeringEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, OccultEngineering.MODID);

    public static final Lazy<EntityType<PucaEntity>> PUCA_TYPE = Lazy.of(
            () -> EntityType.Builder.of(PucaEntity::genericPuca, MobCategory.CREATURE)
                    .sized(0.625f, 1.875f)
                    .clientTrackingRange(8)
                    .build(OccultEngineering.asResource("puca").toString())
    );

    public static final Supplier<EntityType<PucaEntity>> PUCA = ENTITIES.register("puca", PUCA_TYPE);

    public static void register(IEventBus modEventBus) {
        ENTITIES.register(modEventBus);
    }

    public static void registerEntityAttributes(final EntityAttributeCreationEvent event) {
        event.put(PUCA.get(), PucaEntity.createAttributes().build());
    }

    public static void clientRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(PUCA.get(), PucaRenderer::new);
    }
}
