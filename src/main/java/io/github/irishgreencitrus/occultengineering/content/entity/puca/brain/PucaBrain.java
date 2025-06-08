package io.github.irishgreencitrus.occultengineering.content.entity.puca.brain;

import io.github.irishgreencitrus.occultengineering.content.entity.brain.DynamicBrain;
import io.github.irishgreencitrus.occultengineering.content.entity.puca.PucaEntity;

public abstract class PucaBrain extends DynamicBrain<PucaEntity> {
    public PucaBrain(PucaEntity entity) {
        super(entity);
    }
}
