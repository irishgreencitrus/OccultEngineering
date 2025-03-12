package io.github.irishgreencitrus.occultengineering.block.mechanical_pulverizer;

import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringPartialModels;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;

public class PulverizerRenderer extends KineticBlockEntityRenderer<PulverizerBlockEntity> {
    public PulverizerRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected SuperByteBuffer getRotatedModel(PulverizerBlockEntity be, BlockState state) {
        return CachedBuffers.partialFacing(OccultEngineeringPartialModels.SHAFT_QUARTER, state, state.getValue(PulverizerBlock.HORIZONTAL_FACING).getOpposite());
    }
}
