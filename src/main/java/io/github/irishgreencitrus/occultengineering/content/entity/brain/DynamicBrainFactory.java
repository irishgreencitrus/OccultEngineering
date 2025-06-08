package io.github.irishgreencitrus.occultengineering.content.entity.brain;

import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringBrains;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class DynamicBrainFactory<T extends LivingEntity & SmartBrainOwner<T>> {
    Function<T, ? extends DynamicBrain<T>> constructor;
    Class<T> entityClass;

    public DynamicBrainFactory(Function<T, ? extends DynamicBrain<T>> constructor, Class<T> entityClass) {
        this.constructor = constructor;
        this.entityClass = entityClass;
    }

    public DynamicBrain<T> create(T entity) {
        var brain = this.constructor.apply(entity);
        brain.brainID = OccultEngineeringBrains.REGISTRY.get().getKey(this);
        return brain;
    }

    public boolean canCreateFor(@NotNull Class<?> cls) {
        return entityClass.isAssignableFrom(cls);
    }

    public @Nullable DynamicBrain<? extends LivingEntity> create(Entity entity) {
        if (!canCreateFor(entityClass)) {
            return null;
        }
        @SuppressWarnings("unchecked")
        var brain = this.constructor.apply((T) entity);

        brain.brainID = OccultEngineeringBrains.REGISTRY.get().getKey(this);
        return brain;
    }
}
