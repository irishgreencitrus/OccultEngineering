package io.github.irishgreencitrus.occultengineering.registry;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import io.github.irishgreencitrus.occultengineering.block.MechanicalChamberBlockEntity;
import io.github.irishgreencitrus.occultengineering.kinetics.base.TopShaftInstance;
import io.github.irishgreencitrus.occultengineering.render.blockentity.MechanicalChamberRenderer;

import static io.github.irishgreencitrus.occultengineering.OccultEngineering.REGISTRATE;

public class OccultEngineeringBlockEntities {
    public static final BlockEntityEntry<MechanicalChamberBlockEntity> MECHANICAL_CHAMBER =
            REGISTRATE
                    .blockEntity("mechanical_chamber", MechanicalChamberBlockEntity::new)
                    .instance(() -> TopShaftInstance::new)
                    .validBlocks(OccultEngineeringBlocks.MECHANICAL_CHAMBER)
                    .renderer(() -> MechanicalChamberRenderer::new)
                    .register();

    public static void register() {
    }
}
