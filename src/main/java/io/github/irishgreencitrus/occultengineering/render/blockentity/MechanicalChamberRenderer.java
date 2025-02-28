package io.github.irishgreencitrus.occultengineering.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import io.github.irishgreencitrus.occultengineering.block.MechanicalChamberBlockEntity;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringPartialModels;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MechanicalChamberRenderer extends KineticBlockEntityRenderer<MechanicalChamberBlockEntity> {
    public MechanicalChamberRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void renderSafe(MechanicalChamberBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        super.renderSafe(blockEntity, partialTicks, poseStack, buffer, combinedLight, combinedOverlay);
        if (blockEntity.itemStackHandler == null) {
            OccultEngineering.LOGGER.warn("itemStackHandler is null");
            return;
        }

        var stack = blockEntity.itemStackHandler.getStackInSlot(0);
        long time = blockEntity.getLevel().getGameTime();

        var facing = Direction.UP;

        poseStack.pushPose();

        poseStack.pushPose();

        //slowly bob up and down following a sine
        double offset = Math.sin((time - blockEntity.lastChangeTime + partialTicks) / 16) * 0.5f + 0.5f; // * 0.5f + 0.5f;  move sine between 0.0-1.0
        offset = offset / 7.0f; //reduce amplitude

        // Fixed offset to push the item away from the bowl
        double fixedOffset = 0.2;

        // Adjust the translation based on the facing direction
        double yOffset = facing.getAxisDirection() == Direction.AxisDirection.POSITIVE ? offset + fixedOffset : -offset - fixedOffset;

        poseStack.translate(0.5, 0.15 + yOffset, 0.5);

        //use system time to become independent of game time
        long systemTime = blockEntity.getLevel().getGameTime();
        //rotate item slowly around y-axis
        float angle = (systemTime / 16) % 360;
        poseStack.mulPose(Axis.YP.rotationDegrees(angle));

        //Fixed scale
        float scale = 0.5f;
        poseStack.scale(scale, scale, scale);

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        BakedModel model = itemRenderer.getModel(stack, blockEntity.getLevel(), null, 0);
        itemRenderer.render(stack, ItemDisplayContext.FIXED, true, poseStack, buffer,
                combinedLight, combinedOverlay, model);

        poseStack.popPose();

        poseStack.mulPose(facing.getRotation());

        poseStack.popPose();
    }

    @Override
    protected SuperByteBuffer getRotatedModel(MechanicalChamberBlockEntity be, BlockState state) {
        return CachedBufferer.partialFacing(OccultEngineeringPartialModels.TOP_SHAFT, state, Direction.UP);
    }
}
