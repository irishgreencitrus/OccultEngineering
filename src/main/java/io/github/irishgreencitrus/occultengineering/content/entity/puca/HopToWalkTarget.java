package io.github.irishgreencitrus.occultengineering.content.entity.puca;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.util.BrainUtils;


public class HopToWalkTarget<E extends PathfinderMob> extends MoveToWalkTarget<E> {
    private int jumpDelayTicks = 0;

    protected void startOnNewPath(E entity) {
        BrainUtils.setMemory(entity, MemoryModuleType.PATH, this.path);
        entity.getNavigation().moveTo(this.path, this.speedModifier);

        entity.getJumpControl().jump();
        jumpDelayTicks = 10;

    }

    @Override
    protected void tick(E entity) {
        super.tick(entity);
        if (jumpDelayTicks > 0) {
            jumpDelayTicks--;
        }

        var nav = entity.getNavigation();
        if (nav.isInProgress() && entity.onGround() && jumpDelayTicks == 0) {
            entity.getJumpControl().jump();
            jumpDelayTicks = entity.getRandom().nextInt(10, 20);
        }
    }
}

