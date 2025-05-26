package io.github.irishgreencitrus.occultengineering.content.block.pentacle_altar;

import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.item.ItemHelper;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringBlockEntities;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class PentacleAltarBlock extends Block implements IBE<PentacleAltarBlockEntity> {
    public PentacleAltarBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.BLOCK;
    }

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }
        withBlockEntityDo(level, pos, be -> NetworkHooks.openScreen((ServerPlayer) player, be, be::sendToMenu));
        return InteractionResult.SUCCESS;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.hasBlockEntity() || state.getBlock() == newState.getBlock())
            return;
        withBlockEntityDo(level, pos, be -> ItemHelper.dropContents(level, pos, be.inventory));
        level.removeBlockEntity(pos);
    }

    @Override
    public Class<PentacleAltarBlockEntity> getBlockEntityClass() {
        return PentacleAltarBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends PentacleAltarBlockEntity> getBlockEntityType() {
        return OccultEngineeringBlockEntities.PENTACLE_ALTAR.get();
    }
}
