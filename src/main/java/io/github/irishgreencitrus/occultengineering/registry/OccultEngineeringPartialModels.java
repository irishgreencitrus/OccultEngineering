package io.github.irishgreencitrus.occultengineering.registry;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import io.github.irishgreencitrus.occultengineering.OccultEngineering;

public class OccultEngineeringPartialModels {
    public static final PartialModel SHAFT_QUARTER = PartialModel.of(OccultEngineering.asResource("block/shaft_quarter"));
    public static final PartialModel SHAFT_SPLIT = PartialModel.of(OccultEngineering.asResource("block/shaft_split"));
    public static final PartialModel COMBINED_GOGGLES = PartialModel.of(OccultEngineering.asResource("block/combined_goggles"));

    public static void register() {
    }
}
