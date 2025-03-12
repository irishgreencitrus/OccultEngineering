package io.github.irishgreencitrus.occultengineering.registry;

import com.simibubi.create.content.kinetics.base.OrientedRotatingVisual;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import dev.engine_room.flywheel.lib.model.Models;
import io.github.irishgreencitrus.occultengineering.block.mechanical_chamber.MechanicalChamberBlockEntity;
import io.github.irishgreencitrus.occultengineering.block.mechanical_chamber.MechanicalChamberRenderer;
import io.github.irishgreencitrus.occultengineering.block.mechanical_pulverizer.PulverizerBlockEntity;
import io.github.irishgreencitrus.occultengineering.block.mechanical_pulverizer.PulverizerRenderer;
import io.github.irishgreencitrus.occultengineering.block.otherworld_detector.OtherworldDetectorBlockEntity;
import net.minecraft.core.Direction;

import static io.github.irishgreencitrus.occultengineering.OccultEngineering.REGISTRATE;

public class OccultEngineeringBlockEntities {
    public static final BlockEntityEntry<MechanicalChamberBlockEntity> MECHANICAL_CHAMBER =
            REGISTRATE
                    .blockEntity("mechanical_chamber", MechanicalChamberBlockEntity::new)
                    .visual(() -> (context, blockEntity, partialTick)
                            -> new OrientedRotatingVisual<>(context, blockEntity, partialTick, Direction.SOUTH, Direction.UP, Models.partial(OccultEngineeringPartialModels.SHAFT_QUARTER)))
                    .validBlocks(OccultEngineeringBlocks.MECHANICAL_CHAMBER)
                    .renderer(() -> MechanicalChamberRenderer::new)
                    .register();

    public static final BlockEntityEntry<OtherworldDetectorBlockEntity> OTHERWORLD_DETECTOR =
            REGISTRATE.blockEntity("otherworld_detector", OtherworldDetectorBlockEntity::new)
                    .validBlocks(OccultEngineeringBlocks.OTHERWORLD_DETECTOR)
                    .register();

    public static final BlockEntityEntry<PulverizerBlockEntity> MECHANICAL_PULVERIZER =
            REGISTRATE.blockEntity("mechanical_pulverizer", PulverizerBlockEntity::new)
                    .visual(() -> OrientedRotatingVisual.backHorizontal(OccultEngineeringPartialModels.SHAFT_QUARTER))
                    .validBlocks(OccultEngineeringBlocks.MECHANICAL_PULVERIZER)
                    .renderer(() -> PulverizerRenderer::new)
                    .register();

    public static void register() {
    }
}
