package io.github.irishgreencitrus.occultengineering.content.entity.puca.brain;

import com.mojang.datafixers.util.Pair;
import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringBlocks;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.object.TriPredicate;
import net.tslat.smartbrainlib.registry.SBLMemoryTypes;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.apache.commons.lang3.function.TriFunction;

import java.util.List;

public class PlaceBlock<E extends PathfinderMob> extends ExtendedBehaviour<E> {
    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS =
            ObjectArrayList.of(Pair.of(SBLMemoryTypes.NEARBY_BLOCKS.get(), MemoryStatus.VALUE_PRESENT));

    protected TriPredicate<E, BlockPos, BlockState> shouldPlaceNewBlock =
            (entity, pos, state) ->
                    state.is(Blocks.AIR);

    protected TriFunction<E, BlockPos, BlockState, BlockState> stateToPlace =
            (entity, pos, state) ->
                    OccultEngineeringBlocks.STERLING_SILVER_BLOCK.getDefaultState();

    protected BlockPos blockPos = null;
    protected BlockState newState = null;
    protected boolean hasPlacedBlock = false;


    public PlaceBlock<E> shouldPlaceIf(TriPredicate<E, BlockPos, BlockState> shouldPlaceNewBlock) {
        this.shouldPlaceNewBlock = shouldPlaceNewBlock;
        return this;
    }

    public PlaceBlock<E> stateToPlace(TriFunction<E, BlockPos, BlockState, BlockState> stateToPlace) {
        this.stateToPlace = stateToPlace;
        return this;
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        if (hasPlacedBlock) return false;
        for (Pair<BlockPos, BlockState> pair : BrainUtils.getMemory(entity, SBLMemoryTypes.NEARBY_BLOCKS.get())) {
            if (this.shouldPlaceNewBlock.test(entity, pair.getFirst(), pair.getSecond())) {
                this.blockPos = pair.getFirst();
                this.newState = stateToPlace.apply(entity, pair.getFirst(), pair.getSecond());
                return true;
            }
        }
        return false;
    }

    @Override
    protected void stop(E entity) {
        this.hasPlacedBlock = false;
        this.blockPos = null;
        this.newState = null;
    }

    @Override
    protected boolean shouldKeepRunning(E entity) {
        return !hasPlacedBlock;
    }

    @Override
    protected void start(ServerLevel level, E entity, long gameTime) {
        super.start(level, entity, gameTime);
        if (this.blockPos == null || this.newState == null) {
            OccultEngineering.LOGGER.info("Oops all null");
            return;
        }
        OccultEngineering.LOGGER.info("Starting placing block {}", newState);
        if (!hasPlacedBlock) {
            level.setBlock(this.blockPos, this.newState, Block.UPDATE_ALL);
            hasPlacedBlock = true;
            doStop(level, entity, level.getGameTime());
        }
    }
}
