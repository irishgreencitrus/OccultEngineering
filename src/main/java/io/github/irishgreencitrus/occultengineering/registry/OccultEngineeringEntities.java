package io.github.irishgreencitrus.occultengineering.registry;

import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import io.github.irishgreencitrus.occultengineering.content.entity.puca.PucaEntity;
import io.github.irishgreencitrus.occultengineering.content.entity.puca.PucaRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.util.NonNullLazy;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class OccultEngineeringEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, OccultEngineering.MODID);

    public static final NonNullLazy<EntityType<PucaEntity>> PUCA_TYPE = NonNullLazy.of(
            () -> EntityType.Builder.of(PucaEntity::genericPuca, MobCategory.CREATURE)
                    .sized(0.625f, 1.875f)
                    .clientTrackingRange(8)
                    .build(OccultEngineering.asResource("puca").toString())
    );

    public static final RegistryObject<EntityType<PucaEntity>> PUCA = ENTITIES.register("puca", PUCA_TYPE::get);

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
