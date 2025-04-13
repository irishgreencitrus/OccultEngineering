package io.github.irishgreencitrus.occultengineering.registry;

import com.klikli_dev.occultism.common.block.SacrificialBowlBlock;
import com.klikli_dev.occultism.common.block.storage.StableWormholeBlock;
import com.klikli_dev.occultism.common.block.storage.StorageControllerBlock;
import com.simibubi.create.api.registry.CreateBuiltInRegistries;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPointType;
import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import io.github.irishgreencitrus.occultengineering.kinetics.mechanicalArm.BlockArmInteractionPoint;
import io.github.irishgreencitrus.occultengineering.kinetics.mechanicalArm.MechanicalChamberInteractionPoint;
import net.minecraft.core.Registry;

public class OccultEngineeringArmInteractionPoints {
    static {
        register("dimensional_storage", new BlockArmInteractionPoint<>(StorageControllerBlock.class));
        register("mechanical_chamber", new MechanicalChamberInteractionPoint());
        register("sacrificial_bowl", new BlockArmInteractionPoint<>(SacrificialBowlBlock.class));
        register("stable_wormhole", new BlockArmInteractionPoint<>(StableWormholeBlock.class));
    }

    private static <T extends ArmInteractionPointType> void register(String name, T type) {
        Registry.register(CreateBuiltInRegistries.ARM_INTERACTION_POINT_TYPE, OccultEngineering.asResource(name), type);
    }

    public static void init() {

    }
}
