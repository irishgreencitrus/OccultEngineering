package io.github.irishgreencitrus.occultengineering.registry;

import com.jozufozu.flywheel.core.PartialModel;
import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import net.minecraft.resources.ResourceLocation;

public class OccultEngineeringPartialModels {
    public static final PartialModel TOP_SHAFT = new PartialModel(ResourceLocation.fromNamespaceAndPath(OccultEngineering.MODID, "block/top_shaft"));

    public static void register() {
    }
}
