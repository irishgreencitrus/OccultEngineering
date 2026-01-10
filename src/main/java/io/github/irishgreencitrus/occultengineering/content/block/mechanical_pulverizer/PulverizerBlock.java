package io.github.irishgreencitrus.occultengineering.content.block.mechanical_pulverizer;

import com.simibubi.create.AllShapes;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import io.github.irishgreencitrus.occultengineering.content.block.OcEngBlockStates;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringBlockEntities;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringDataComponents;
import net.createmod.catnip.math.VoxelShaper;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
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
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(OcEngBlockStates.TIER);
        super.createBlockStateDefinition(builder);
    }

    @Override
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
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.isClientSide) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        // If we don't do this, this function is triggered twice
        if (hand == InteractionHand.OFF_HAND) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        var be = getBlockEntity(level, pos);
        if (be == null) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        var hasHandInteraction = player.getItemInHand(hand).isEmpty() || be.canProcess(player.getItemInHand(hand));
        if (!hasHandInteraction) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

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

            pulverizer.notifyUpdate();
        });

        return ItemInteractionResult.SUCCESS;
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(worldIn, pos, state, placer, stack);
        if (worldIn.isClientSide) return;
        withBlockEntityDo(worldIn, pos, be -> be.setTier(stack.getOrDefault(OccultEngineeringDataComponents.CRUSHING_ITEM_TIER, 1)));
    }

    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
       var drops = super.getDrops(state, builder);

       var be = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
       if (be instanceof PulverizerBlockEntity pbe) {
           for (ItemStack stack : drops) {
               if (stack.is(asItem())) {
                   stack.set(OccultEngineeringDataComponents.CRUSHING_ITEM_TIER, pbe.getTier());
               }
           }
       }

       return drops;
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
    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
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
