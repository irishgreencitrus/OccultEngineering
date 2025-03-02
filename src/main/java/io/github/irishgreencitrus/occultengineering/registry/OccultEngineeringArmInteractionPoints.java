package io.github.irishgreencitrus.occultengineering.registry;

import com.simibubi.create.api.registry.CreateBuiltInRegistries;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPointType;
import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import io.github.irishgreencitrus.occultengineering.kinetics.mechanicalArm.DimensionalStorageActuatorInteractionPoint;
import io.github.irishgreencitrus.occultengineering.kinetics.mechanicalArm.MechanicalChamberInteractionPoint;
import io.github.irishgreencitrus.occultengineering.kinetics.mechanicalArm.SacrificialBowlInteractionPoint;
import io.github.irishgreencitrus.occultengineering.kinetics.mechanicalArm.StableWormholeInteractionPoint;
import net.minecraft.core.Registry;
import org.jetbrains.annotations.ApiStatus;

public class OccultEngineeringArmInteractionPoints {
    static {
        register("dimensional_storage", new DimensionalStorageActuatorInteractionPoint());
        register("mechanical_chamber", new MechanicalChamberInteractionPoint());
        register("sacrificial_bowl", new SacrificialBowlInteractionPoint());
        register("stable_wormhole", new StableWormholeInteractionPoint());
    }

    private static <T extends ArmInteractionPointType> void register(String name, T type) {
        Registry.register(CreateBuiltInRegistries.ARM_INTERACTION_POINT_TYPE, OccultEngineering.asResource(name), type);
    }

    @ApiStatus.Internal
    public static void init() {

    }
}
