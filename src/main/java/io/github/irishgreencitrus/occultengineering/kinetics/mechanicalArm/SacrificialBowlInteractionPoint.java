package io.github.irishgreencitrus.occultengineering.kinetics.mechanicalArm;

import com.klikli_dev.occultism.common.block.SacrificialBowlBlock;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPoint;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPointType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class SacrificialBowlInteractionPoint extends ArmInteractionPointType {
    public SacrificialBowlInteractionPoint(ResourceLocation id) {
        super(id);
    }

    @Override
    public boolean canCreatePoint(Level level, BlockPos blockPos, BlockState blockState) {
        return (blockState.getBlock() instanceof SacrificialBowlBlock);
    }

    @Nullable
    @Override
    public ArmInteractionPoint createPoint(Level level, BlockPos blockPos, BlockState blockState) {
        return new ArmInteractionPoint(this, level, blockPos, blockState);
    }
}
