package io.github.irishgreencitrus.occultengineering.registry;

import com.simibubi.create.content.kinetics.base.OrientedRotatingVisual;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import io.github.irishgreencitrus.occultengineering.block.MechanicalChamberBlockEntity;
import io.github.irishgreencitrus.occultengineering.render.blockentity.MechanicalChamberRenderer;
import org.jetbrains.annotations.ApiStatus;

import static io.github.irishgreencitrus.occultengineering.OccultEngineering.REGISTRATE;

public class OccultEngineeringBlockEntities {
    public static final BlockEntityEntry<MechanicalChamberBlockEntity> MECHANICAL_CHAMBER =
            REGISTRATE
                    .blockEntity("mechanical_chamber", MechanicalChamberBlockEntity::new)
                    .visual(() -> OrientedRotatingVisual.of(OccultEngineeringPartialModels.TOP_SHAFT), false)
                    .validBlocks(OccultEngineeringBlocks.MECHANICAL_CHAMBER)
                    .renderer(() -> MechanicalChamberRenderer::new)
                    .register();

    @ApiStatus.Internal
    public static void init() {
    }
}
