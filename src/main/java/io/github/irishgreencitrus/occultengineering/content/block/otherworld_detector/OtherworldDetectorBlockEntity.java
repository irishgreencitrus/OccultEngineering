package io.github.irishgreencitrus.occultengineering.content.block.otherworld_detector;

import com.klikli_dev.occultism.Occultism;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class OtherworldDetectorBlockEntity extends SmartBlockEntity {
    // TODO: provide a codec for this block entity so it can be properly networked.
    public OtherworldDetectorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
        setLazyTickRate(20);
    }

    public int turnOffInTicks = 0;
    public int comparatorSignalStrength = 0;

    @Override
    public void tick() {
        super.tick();
        if (level == null) return;
        if (level.isClientSide) return;
        BlockState state = getBlockState();
        if (turnOffInTicks > 0) {
            turnOffInTicks--;
            if (turnOffInTicks == 0) {
                level.scheduleTick(worldPosition, state.getBlock(), 1);
            }
        }

        var nearestPlayer = level.getNearestPlayer(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), 15.0, pl -> !pl.isSpectator());

        if (nearestPlayer == null) return;
        if (Occultism.THIRD_EYE_EFFECT_RENDERER.gogglesActiveLastTick || Occultism.THIRD_EYE_EFFECT_RENDERER.thirdEyeActiveLastTick) {
            var distanceSqr = nearestPlayer.distanceToSqr(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ());
            var distance = Math.sqrt(distanceSqr);
            // Stronger the closer you are
            comparatorSignalStrength = 15 - (int) distance;

            activate(4);
        }
    }

    public void activate(int ticks) {
        if (level == null) return;

        BlockState state = getBlockState();
        turnOffInTicks = ticks;
        //if (state.getValue(OtherworldDetectorBlock.POWERED)) return;
        level.setBlockAndUpdate(worldPosition, state.setValue(OtherworldDetectorBlock.POWERED, true));
        level.updateNeighborsAt(worldPosition, state.getBlock());
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> list) {
    }

}
