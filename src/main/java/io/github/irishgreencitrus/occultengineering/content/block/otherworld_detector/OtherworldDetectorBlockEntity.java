package io.github.irishgreencitrus.occultengineering.content.block.otherworld_detector;

import com.klikli_dev.occultism.Occultism;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import io.github.irishgreencitrus.occultengineering.content.block.otherworld_detector.packet.ThirdEyeActivationPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class OtherworldDetectorBlockEntity extends SmartBlockEntity {
    public OtherworldDetectorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
        setLazyTickRate(20);
    }

    public int turnOffInTicks = 0;
    public int comparatorSignalStrength = 0;

    @Override
    public void tick() {
        super.tick();
        if (level == null || level.isClientSide) return;

        BlockState state = getBlockState();
        if (turnOffInTicks > 0) {
            turnOffInTicks--;
            if (turnOffInTicks == 0) {
                level.setBlockAndUpdate(worldPosition, state.setValue(OtherworldDetectorBlock.POWERED, false));
                level.updateNeighborsAt(worldPosition, state.getBlock());
            }
        }
        var centre = worldPosition.getCenter();

        var nearestPlayer = level.getNearestPlayer(centre.x, centre.y, centre.z, 15.0, pl -> !pl.isSpectator());
        if (nearestPlayer == null) return;

        if (ThirdEyeActivationPacket.isActive(nearestPlayer)) {
            var distanceSqr = nearestPlayer.distanceToSqr(centre.x, centre.y, centre.z);
            var distance = Math.sqrt(distanceSqr);
            // Stronger the closer you are
            var newSignal = Math.max(0,15 - (int) distance);
            activate(newSignal,4);
        }
    }

    public void activate(int signalStrength, int ticks) {
        if (level == null) return;

        BlockState state = getBlockState();
        turnOffInTicks = ticks;
        if (signalStrength != comparatorSignalStrength) {
            comparatorSignalStrength = signalStrength;
            level.setBlockAndUpdate(worldPosition, state.setValue(OtherworldDetectorBlock.POWERED, true));
            level.updateNeighborsAt(worldPosition, state.getBlock());
        }
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> list) {
    }
}
