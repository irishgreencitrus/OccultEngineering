package io.github.irishgreencitrus.occultengineering.kinetics.base;

import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.MaterialManager;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.SingleRotatingInstance;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringPartialModels;
import net.minecraft.core.Direction;

public class TopShaftInstance<T extends KineticBlockEntity> extends SingleRotatingInstance<T> {
    public TopShaftInstance(MaterialManager materialManager, T blockEntity) {
        super(materialManager, blockEntity);
    }

    protected Instancer<RotatingData> getModel() {
        Direction dir = this.getShaftDirection();
        return this.getRotatingMaterial().getModel(OccultEngineeringPartialModels.TOP_SHAFT, this.blockState, dir);
    }

    protected Direction getShaftDirection() {
        return Direction.UP;
    }
}