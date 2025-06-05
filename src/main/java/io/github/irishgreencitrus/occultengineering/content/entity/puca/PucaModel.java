package io.github.irishgreencitrus.occultengineering.content.entity.puca;

import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import net.minecraft.util.Mth;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class PucaModel extends DefaultedEntityGeoModel<PucaEntity> {
    public PucaModel() {
        super(OccultEngineering.asResource("puca"));
    }

    @Override
    public void setCustomAnimations(PucaEntity animatable, long instanceId, AnimationState<PucaEntity> animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("Head");

        if (head != null) {
            var entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotX(entityData.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
        }
    }
}
