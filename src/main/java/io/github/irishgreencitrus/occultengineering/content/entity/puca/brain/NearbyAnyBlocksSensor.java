package io.github.irishgreencitrus.occultengineering.content.entity.puca.brain;

import net.minecraft.world.entity.LivingEntity;
import net.tslat.smartbrainlib.api.core.sensor.custom.NearbyBlocksSensor;

public class NearbyAnyBlocksSensor<E extends LivingEntity> extends NearbyBlocksSensor<E> {
    public NearbyAnyBlocksSensor() {
        super();
        setPredicate((blockState, e) -> true);
    }
}
