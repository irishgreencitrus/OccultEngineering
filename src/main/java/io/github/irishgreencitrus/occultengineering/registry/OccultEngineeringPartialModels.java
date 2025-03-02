package io.github.irishgreencitrus.occultengineering.registry;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;

public class OccultEngineeringPartialModels {
    public static final PartialModel TOP_SHAFT = PartialModel.of(ResourceLocation.fromNamespaceAndPath(OccultEngineering.MODID, "block/top_shaft"));

    @ApiStatus.Internal
    public static void init() {
    }
}
