package io.github.irishgreencitrus.occultengineering.content.entity.puca.brain;

import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import io.github.irishgreencitrus.occultengineering.content.entity.puca.PucaEntity;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;

public class PucaEmptyBrain extends PucaBrain {
    public PucaEmptyBrain(PucaEntity entity) {
        super(entity);
    }

    @Override
    public void tick() {

    }

    @Override
    protected void onInit() {
        OccultEngineering.LOGGER.info("An empty brain has been initialised");
    }

    @Override
    public void onCleanup() {

    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag compoundTag) {
    }
}
