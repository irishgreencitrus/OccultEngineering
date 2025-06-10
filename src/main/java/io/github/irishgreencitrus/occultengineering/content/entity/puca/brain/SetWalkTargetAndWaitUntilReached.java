package io.github.irishgreencitrus.occultengineering.content.entity.puca.brain;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class SetWalkTargetAndWaitUntilReached<E extends PathfinderMob> extends ExtendedBehaviour<E> {
    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(
            Pair.of(MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED),
            Pair.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED));

    protected Function<E, BlockPos> targetSupplier;
    protected Function<E, Integer> timeoutSupplier;
    protected BiFunction<E, BlockPos, Float> speedMod = (owner, pos) -> 1f;
    protected BiFunction<E, BlockPos, Integer> closeEnoughDist = (entity, pos) -> 2;

    protected BlockPos targetPosition = null;
    protected int timeoutTicks = 20 * 10;

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    /**
     * Set the target for the entity.
     *
     * @param targetSupplier The target
     * @return this
     */
    public SetWalkTargetAndWaitUntilReached<E> target(Function<E, BlockPos> targetSupplier) {
        this.targetSupplier = targetSupplier;

        return this;
    }

    /**
     * Set the timeout for this task.
     *
     * @param timeoutSupplier The target
     * @return this
     */
    public SetWalkTargetAndWaitUntilReached<E> setTimeout(Function<E, Integer> timeoutSupplier) {
        this.timeoutSupplier = timeoutSupplier;

        return this;
    }

    /**
     * Set the movespeed modifier for the entity when moving to the target.
     *
     * @param speedModifier The movespeed modifier/multiplier
     * @return this
     */
    public SetWalkTargetAndWaitUntilReached<E> speedMod(BiFunction<E, BlockPos, Float> speedModifier) {
        this.speedMod = speedModifier;

        return this;
    }

    /**
     * Set the distance (in blocks) that is 'close enough' for the entity to be considered at the target position
     *
     * @param function The function
     * @return this
     */
    public SetWalkTargetAndWaitUntilReached<E> closeEnoughWhen(final BiFunction<E, BlockPos, Integer> function) {
        this.closeEnoughDist = function;

        return this;
    }

    @Override
    protected boolean checkExtraStartConditions(@NotNull ServerLevel level, @NotNull E entity) {
        if (targetSupplier == null || timeoutSupplier == null) return false;
        var t = targetSupplier.apply(entity);
        if (t == null) return false;

        if (level.isLoaded(t)) {
            targetPosition = t;
        }
        return this.targetPosition != null;
    }

    @Override
    protected void start(E entity) {
        if (targetPosition != null) {
            BrainUtils.setMemory(entity, MemoryModuleType.WALK_TARGET, new WalkTarget(this.targetPosition, this.speedMod.apply(entity, this.targetPosition), this.closeEnoughDist.apply(entity, this.targetPosition)));
            BrainUtils.setMemory(entity, MemoryModuleType.LOOK_TARGET, new BlockPosTracker(this.targetPosition));
        } else {
            BrainUtils.clearMemory(entity, MemoryModuleType.WALK_TARGET);
        }
    }

    @Override
    protected boolean shouldKeepRunning(E entity) {
        if (timeoutTicks <= 0) return false;
        // We haven't actually started yet, so we don't want to stop already.
        if (targetPosition == null || closeEnoughDist == null) return true;
        return entity.position().distanceToSqr(this.targetPosition.getCenter()) > this.closeEnoughDist.apply(entity, entity.blockPosition());
    }

    @Override
    protected void tick(E entity) {
        if (timeoutTicks > 0) timeoutTicks--;
    }

    @Override
    protected void stop(E entity) {
        this.targetPosition = null;
    }
}
