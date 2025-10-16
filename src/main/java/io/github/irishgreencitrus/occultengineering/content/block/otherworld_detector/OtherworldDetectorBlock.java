package io.github.irishgreencitrus.occultengineering.content.block.otherworld_detector;

import com.simibubi.create.foundation.block.IBE;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringBlockEntities;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class OtherworldDetectorBlock extends Block implements IBE<OtherworldDetectorBlockEntity> {

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public OtherworldDetectorBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(POWERED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(POWERED));
    }

    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, @Nullable Direction direction) {
        return true;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        IBE.onRemove(state, level, pos, newState);
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return state.getValue(POWERED);
    }

    @Override
    public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        BlockState toState = level.getBlockState(pos.relative(direction.getOpposite()));
        if (toState.is(this))
            return 0;
        if (toState.is(Blocks.COMPARATOR)) {
            var be = getBlockEntity(level, pos);
            if (be == null) return 0;
            return isSignalSource(state) ? be.comparatorSignalStrength : 0;
        }
        return isSignalSource(state) ? 15 : 0;
    }

    @Override
    public Class<OtherworldDetectorBlockEntity> getBlockEntityClass() {
        return OtherworldDetectorBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends OtherworldDetectorBlockEntity> getBlockEntityType() {
        return OccultEngineeringBlockEntities.OTHERWORLD_DETECTOR.get();
    }
}
