package io.github.irishgreencitrus.occultengineering.registry;

import com.simibubi.create.content.kinetics.base.OrientedRotatingVisual;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import dev.engine_room.flywheel.lib.model.Models;
import io.github.irishgreencitrus.occultengineering.block.mechanical_chamber.MechanicalChamberBlockEntity;
import io.github.irishgreencitrus.occultengineering.block.otherworld_detector.OtherworldDetectorBlockEntity;
import io.github.irishgreencitrus.occultengineering.render.blockentity.MechanicalChamberRenderer;
import net.minecraft.core.Direction;

import static io.github.irishgreencitrus.occultengineering.OccultEngineering.REGISTRATE;

public class OccultEngineeringBlockEntities {
    public static final BlockEntityEntry<MechanicalChamberBlockEntity> MECHANICAL_CHAMBER =
            REGISTRATE
                    .blockEntity("mechanical_chamber", MechanicalChamberBlockEntity::new)
                    .visual(() -> (context, blockEntity, partialTick)
                            -> new OrientedRotatingVisual<>(context, blockEntity, partialTick, Direction.SOUTH, Direction.UP, Models.partial(OccultEngineeringPartialModels.TOP_SHAFT)))
                    .validBlocks(OccultEngineeringBlocks.MECHANICAL_CHAMBER)
                    .renderer(() -> MechanicalChamberRenderer::new)
                    .register();

    public static final BlockEntityEntry<OtherworldDetectorBlockEntity> OTHERWORLD_DETECTOR =
            REGISTRATE.blockEntity("otherworld_detector", OtherworldDetectorBlockEntity::new)
                    .validBlocks(OccultEngineeringBlocks.OTHERWORLD_DETECTOR)
                    .register();

    public static void register() {
    }
}
