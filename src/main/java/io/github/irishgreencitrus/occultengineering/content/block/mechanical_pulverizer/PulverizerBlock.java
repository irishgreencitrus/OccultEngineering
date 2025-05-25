package io.github.irishgreencitrus.occultengineering.content.block.mechanical_pulverizer;

import com.simibubi.create.AllShapes;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringBlockEntities;
import net.createmod.catnip.math.VoxelShaper;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PulverizerBlock extends HorizontalKineticBlock implements IBE<PulverizerBlockEntity> {
    private static final VoxelShaper SHAPE = new AllShapes.Builder(Block.box(2, 0, 0, 14, 2, 13))
            .add(5, 2, 3, 11, 11, 10)
            .forHorizontal(Direction.SOUTH);


    public PulverizerBlock(Properties properties) {
        super(properties);
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE.get(state.getValue(HORIZONTAL_FACING));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction preferred = getPreferredHorizontalFacing(context);
        return defaultBlockState().setValue(
                HORIZONTAL_FACING,
                Objects.requireNonNullElseGet(preferred, context::getHorizontalDirection).getOpposite()
        );
    }

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) return InteractionResult.SUCCESS;

        var be = getBlockEntity(level, pos);
        if (be == null) return InteractionResult.PASS;

        var hasHandInteraction = player.getItemInHand(hand).isEmpty() || be.canProcess(player.getItemInHand(hand));
        if (!hasHandInteraction) return InteractionResult.PASS;

        withBlockEntityDo(level, pos, pulverizer -> {
            var mainHandItem = player.getItemInHand(hand);

            if (mainHandItem.isEmpty()) {
                var outputEmpty = true;
                var inv = pulverizer.outputInv;
                for (int slot = 0; slot < inv.getSlots(); slot++) {
                    ItemStack stackInSlot = inv.getStackInSlot(slot);
                    if (!stackInSlot.isEmpty()) outputEmpty = false;
                    player.getInventory().placeItemBackInInventory(stackInSlot);
                    inv.setStackInSlot(slot, ItemStack.EMPTY);
                }
                if (outputEmpty) {
                    inv = pulverizer.inputInv;
                    for (int slot = 0; slot < inv.getSlots(); slot++) {
                        player.getInventory().placeItemBackInInventory(inv.getStackInSlot(slot));
                        inv.setStackInSlot(slot, ItemStack.EMPTY);
                    }
                }
            } else if (pulverizer.inputInv.getStackInSlot(0).isEmpty()) {
                player.setItemInHand(
                        hand,
                        pulverizer.inputInv.insertItem(0, mainHandItem, false)
                );
            }

            pulverizer.setChanged();
            pulverizer.sendData();
        });

        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face == state.getValue(HORIZONTAL_FACING).getOpposite();
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState blockState) {
        return blockState.getValue(HORIZONTAL_FACING).getAxis();
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Override
    public Class<PulverizerBlockEntity> getBlockEntityClass() {
        return PulverizerBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends PulverizerBlockEntity> getBlockEntityType() {
        return OccultEngineeringBlockEntities.MECHANICAL_PULVERIZER.get();
    }

}
