package io.github.irishgreencitrus.occultengineering.content.block.pucalith;

import com.mojang.serialization.MapCodec;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.item.ItemHelper;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class PucalithBlock extends HorizontalDirectionalBlock implements IBE<PucalithBlockEntity> {
    public PucalithBlock(Properties properties) {
        super(properties);
    }

    public static final MapCodec<PucalithBlock> CODEC = simpleCodec(PucalithBlock::new);

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }
        withBlockEntityDo(level, pos, be ->
                player.openMenu(be, be::sendToMenu));
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
    public @Nullable PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.BLOCK;
    }

    @Override
    public Class<PucalithBlockEntity> getBlockEntityClass() {
        return PucalithBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends PucalithBlockEntity> getBlockEntityType() {
        return OccultEngineeringBlockEntities.PUCALITH.get();
    }
}
