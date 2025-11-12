package io.github.irishgreencitrus.occultengineering.content.item.model;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringPartialModels;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.neoforged.neoforge.client.model.BakedModelWrapper;

public class CombinedGogglesModel extends BakedModelWrapper<BakedModel> {
    public CombinedGogglesModel(BakedModel originalModel) {
        super(originalModel);
    }

    @Override
    public BakedModel applyTransform(ItemDisplayContext cameraTransformType, PoseStack poseStack, boolean applyLeftHandTransform) {
        if (cameraTransformType == ItemDisplayContext.HEAD)
            return OccultEngineeringPartialModels.COMBINED_GOGGLES.get()
                    .applyTransform(cameraTransformType, poseStack, applyLeftHandTransform);
        else return super.applyTransform(cameraTransformType, poseStack, applyLeftHandTransform);
    }
}
