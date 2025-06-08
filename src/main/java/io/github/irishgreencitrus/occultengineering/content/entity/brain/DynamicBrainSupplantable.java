package io.github.irishgreencitrus.occultengineering.content.entity.brain;

import net.minecraft.world.entity.LivingEntity;

public interface DynamicBrainSupplantable {
    void supplantBrain(DynamicBrain<? extends LivingEntity> newBrain);
}
