package io.github.irishgreencitrus.occultengineering.kinetics.mechanicalArm;

import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPoint;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPointType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockArmInteractionPoint<T extends Block> extends ArmInteractionPointType {
    @NotNull
    private final Class<T> blockClass;

    public BlockArmInteractionPoint(@NotNull Class<T> blockClass) {
        this.blockClass = blockClass;
    }

    @Override
    public boolean canCreatePoint(Level level, BlockPos blockPos, BlockState blockState) {
        return blockClass.isInstance(blockState.getBlock());
    }

    @Override
    public @Nullable ArmInteractionPoint createPoint(Level level, BlockPos blockPos, BlockState blockState) {
        return new ArmInteractionPoint(this, level, blockPos, blockState);
    }
}
