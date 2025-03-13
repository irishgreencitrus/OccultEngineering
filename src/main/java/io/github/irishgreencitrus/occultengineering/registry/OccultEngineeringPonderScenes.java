package io.github.irishgreencitrus.occultengineering.registry;

import com.simibubi.create.infrastructure.ponder.AllCreatePonderTags;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import io.github.irishgreencitrus.occultengineering.ponder.OcEngPonders;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.minecraft.resources.ResourceLocation;

public class OccultEngineeringPonderScenes {
    public static void register(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        PonderSceneRegistrationHelper<ItemProviderEntry<?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);
        HELPER.addStoryBoard(OccultEngineeringBlocks.MECHANICAL_PULVERIZER, "mechanical_pulverizer", OcEngPonders::pulverizer, AllCreatePonderTags.KINETIC_APPLIANCES);
    }
}
