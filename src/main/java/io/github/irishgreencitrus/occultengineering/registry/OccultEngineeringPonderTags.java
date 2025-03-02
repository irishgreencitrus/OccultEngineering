package io.github.irishgreencitrus.occultengineering.registry;

import com.klikli_dev.occultism.registry.OccultismBlocks;
import com.simibubi.create.infrastructure.ponder.AllCreatePonderTags;
import net.createmod.catnip.registry.RegisteredObjectsHelper;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;

public class OccultEngineeringPonderTags {
    public static void register(PonderTagRegistrationHelper<ResourceLocation> helper) {
        PonderTagRegistrationHelper<ItemLike> itemHelper = helper.withKeyFunction(
                RegisteredObjectsHelper::getKeyOrThrow);

        itemHelper.addToTag(AllCreatePonderTags.ARM_TARGETS)
                .add(OccultEngineeringBlocks.MECHANICAL_CHAMBER)
                .add(OccultismBlocks.SACRIFICIAL_BOWL)
                .add(OccultismBlocks.STABLE_WORMHOLE)
                .add(OccultismBlocks.STORAGE_CONTROLLER);

        itemHelper.addToTag(AllCreatePonderTags.ARM_TARGETS)
                .add(OccultEngineeringBlocks.MECHANICAL_CHAMBER)
                .add(OccultismBlocks.STABLE_WORMHOLE)
                .add(OccultismBlocks.STORAGE_CONTROLLER)
                .add(OccultEngineeringBlocks.MECHANICAL_CHAMBER);
    }
}
