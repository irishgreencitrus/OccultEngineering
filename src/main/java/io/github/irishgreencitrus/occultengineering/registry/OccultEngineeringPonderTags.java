package io.github.irishgreencitrus.occultengineering.registry;

import com.klikli_dev.occultism.registry.OccultismBlocks;
import com.simibubi.create.foundation.ponder.PonderRegistry;
import com.simibubi.create.infrastructure.ponder.AllPonderTags;

public class OccultEngineeringPonderTags {
    public static void register() {
        PonderRegistry.TAGS.forTag(AllPonderTags.ARM_TARGETS)
                .add(OccultismBlocks.SACRIFICIAL_BOWL.get())
                .add(OccultismBlocks.STABLE_WORMHOLE.get())
                .add(OccultismBlocks.STORAGE_CONTROLLER.get())
                .add(OccultEngineeringBlocks.MECHANICAL_CHAMBER.get());

        PonderRegistry.TAGS.forTag(AllPonderTags.KINETIC_APPLIANCES)
                .add(OccultEngineeringBlocks.MECHANICAL_CHAMBER);
    }
}
