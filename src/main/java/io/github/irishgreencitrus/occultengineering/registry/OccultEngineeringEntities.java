package io.github.irishgreencitrus.occultengineering.registry;

import com.simibubi.create.foundation.data.CreateEntityBuilder;
import com.tterrag.registrate.util.entry.EntityEntry;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import io.github.irishgreencitrus.occultengineering.content.entity.PucaEntity;
import io.github.irishgreencitrus.occultengineering.content.entity.PucaRenderer;
import net.createmod.catnip.lang.Lang;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;

import static io.github.irishgreencitrus.occultengineering.OccultEngineering.REGISTRATE;

public class OccultEngineeringEntities {
    public static final EntityEntry<PucaEntity> PUCA = register(
            "puca", PucaEntity::new, () -> PucaRenderer::new, MobCategory.CREATURE,
            10, 3, true, b -> b.fireImmune()
    ).register();

    public static void register() {

    }

    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(PUCA.get(), PucaEntity.createAttributes().build());
    }


    @SuppressWarnings("SameParameterValue")
    private static <T extends Entity> CreateEntityBuilder<T, ?> register(String name, EntityType.EntityFactory<T> factory,
                                                                         NonNullSupplier<NonNullFunction<EntityRendererProvider.Context, EntityRenderer<? super T>>> renderer,
                                                                         MobCategory group, int range, int updateFrequency, boolean sendVelocity,
                                                                         NonNullConsumer<EntityType.Builder<T>> propertyBuilder) {
        String id = Lang.asId(name);
        return (CreateEntityBuilder<T, ?>) REGISTRATE
                .entity(id, factory, group)
                .properties(b -> b.setTrackingRange(range)
                        .setUpdateInterval(updateFrequency)
                        .setShouldReceiveVelocityUpdates(sendVelocity))
                .properties(propertyBuilder)
                .renderer(renderer);
    }
}
