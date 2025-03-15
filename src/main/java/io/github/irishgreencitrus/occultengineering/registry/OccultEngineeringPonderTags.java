package io.github.irishgreencitrus.occultengineering.registry;

import com.klikli_dev.occultism.registry.OccultismBlocks;
import com.simibubi.create.infrastructure.ponder.AllCreatePonderTags;
import com.tterrag.registrate.util.entry.RegistryEntry;
import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import net.createmod.catnip.platform.CatnipServices;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;

public class OccultEngineeringPonderTags {
    public static final ResourceLocation
            OCCULT_APPLIANCES = loc("occult_appliances");

    public static ResourceLocation loc(String id) {
        return OccultEngineering.asResource(id);
    }
    public static void register(PonderTagRegistrationHelper<ResourceLocation> helper) {

        PonderTagRegistrationHelper<RegistryEntry<?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);

        PonderTagRegistrationHelper<ItemLike> itemHelper = helper.withKeyFunction(
                CatnipServices.REGISTRIES::getKeyOrThrow);

        helper.registerTag(OCCULT_APPLIANCES)
                .addToIndex()
                .item(OccultEngineeringBlocks.MECHANICAL_PULVERIZER, true, false)
                .title("Occult Appliances")
                .description("Appliances added by Create: Occult Engineering")
                .register();

        itemHelper.addToTag(AllCreatePonderTags.ARM_TARGETS)
                .add(OccultismBlocks.SACRIFICIAL_BOWL.get())
                .add(OccultismBlocks.STABLE_WORMHOLE.get())
                .add(OccultismBlocks.STORAGE_CONTROLLER.get())
                .add(OccultEngineeringBlocks.MECHANICAL_CHAMBER.get());

        HELPER.addToTag(AllCreatePonderTags.KINETIC_APPLIANCES)
                .add(OccultEngineeringBlocks.MECHANICAL_CHAMBER)
                .add(OccultEngineeringBlocks.MECHANICAL_PULVERIZER);

        HELPER.addToTag(OCCULT_APPLIANCES)
                .add(OccultEngineeringBlocks.MECHANICAL_CHAMBER)
                .add(OccultEngineeringBlocks.MECHANICAL_PULVERIZER)
                .add(OccultEngineeringBlocks.OTHERWORLD_DETECTOR);

    }
}
