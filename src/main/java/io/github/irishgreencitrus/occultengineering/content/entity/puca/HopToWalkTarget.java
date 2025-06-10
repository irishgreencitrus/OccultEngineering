package io.github.irishgreencitrus.occultengineering.content.entity.puca;

import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;


public class HopToWalkTarget<E extends PucaEntity> extends MoveToWalkTarget<E> {
    private int jumpDelayTicks = 0;

    protected void startOnNewPath(E entity) {
        super.startOnNewPath(entity);

        entity.jumpNow();
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
            entity.jumpNow();
            jumpDelayTicks = entity.getRandom().nextInt(8, 13);
        }
    }
}

