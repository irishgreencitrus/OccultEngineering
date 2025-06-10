package io.github.irishgreencitrus.occultengineering.content.entity.puca.brain;

import io.github.irishgreencitrus.occultengineering.content.entity.puca.HopToWalkTarget;
import io.github.irishgreencitrus.occultengineering.content.entity.puca.PucaEntity;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.behaviour.SequentialBehaviour;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;

public class PucaConstructionBrain extends PucaBrain {
    public PucaConstructionBrain(PucaEntity entity) {
        super(entity);
    }

    @Override
    public void tick() {

    }

    @Override
    protected void onInit() {

    }

    @Override
    public List<ExtendedSensor<PucaEntity>> getSensors() {
        return ObjectArrayList.of(
                new NearbyAnyBlocksSensor<>()
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    public BrainActivityGroup<PucaEntity> getCoreTasks() {
        if (entity.nextTargetPos == null || entity.nextBlockState == null || entity.returnHomePosition == null)
            return BrainActivityGroup.empty();
        return BrainActivityGroup.coreTasks(
                new HopToWalkTarget<>()
                        .runFor((e) -> 20 * 20)
                        .cooldownFor((e) -> 0),
                new PlaceBlock<PucaEntity>()
                        .shouldPlaceIf((e, p, s) -> e.nextTargetPos != null && e.nextTargetPos.equals(p))
                        .stateToPlace((e, p, f) -> e.nextBlockState)
                        .whenStopping((e) -> e.nextBlockState = null)
                        .cooldownFor((e) -> 20 * 5)
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    public BrainActivityGroup<PucaEntity> getIdleTasks() {
        return BrainActivityGroup.idleTasks(new SequentialBehaviour<>(
                new SetWalkTargetAndWaitUntilReached<PucaEntity>()
                        .target((mob) -> mob.nextTargetPos)
                        .setTimeout((e) -> 20 * 30)
                        .closeEnoughWhen((a, b) -> 1)
                        .cooldownFor((e) -> 20 * 2),
                new SetWalkTargetAndWaitUntilReached<PucaEntity>()
                        .target((mob) -> mob.returnHomePosition)
                        .setTimeout((e) -> 20 * 30)
                        .closeEnoughWhen((a, b) -> 4)
                        .cooldownFor((e) -> 20 * 2)
        ));
    }

    @Override
    public void onBrainSetup(Brain<? extends PucaEntity> brain) {
        super.onBrainSetup(brain);
        if (entity.returnHomePosition == null) return;
        BrainUtils.setMemory(brain, MemoryModuleType.HOME, GlobalPos.of(entity.level().dimension(), entity.returnHomePosition));
    }

    @Override
    public void onCleanup() {

    }
}
