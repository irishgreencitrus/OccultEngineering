package io.github.irishgreencitrus.occultengineering.registry;

import com.simibubi.create.api.registry.CreateBuiltInRegistries;
import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import io.github.irishgreencitrus.occultengineering.kinetics.fan.processing.FanEnspiritType;
import net.minecraft.core.Registry;
import org.jetbrains.annotations.ApiStatus;

public class OccultEngineeringFanProcessingTypes {
    static {
        Registry.register(CreateBuiltInRegistries.FAN_PROCESSING_TYPE, OccultEngineering.asResource("enspirit"), new FanEnspiritType());
    }

    @ApiStatus.Internal
    public static void init() {
    }

}
