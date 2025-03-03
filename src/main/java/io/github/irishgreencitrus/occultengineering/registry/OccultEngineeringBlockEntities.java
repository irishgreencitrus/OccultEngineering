package io.github.irishgreencitrus.occultengineering.registry;

import com.simibubi.create.content.kinetics.base.OrientedRotatingVisual;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import dev.engine_room.flywheel.lib.model.Models;
import io.github.irishgreencitrus.occultengineering.block.MechanicalChamberBlockEntity;
import io.github.irishgreencitrus.occultengineering.render.blockentity.MechanicalChamberRenderer;
import net.minecraft.core.Direction;

import static io.github.irishgreencitrus.occultengineering.OccultEngineering.REGISTRATE;

public class OccultEngineeringBlockEntities {
    public static final BlockEntityEntry<MechanicalChamberBlockEntity> MECHANICAL_CHAMBER =
            REGISTRATE
                    .blockEntity("mechanical_chamber", MechanicalChamberBlockEntity::new)
                    .visual(() -> (context, blockEntity, partialTick) -> {
                        return new OrientedRotatingVisual<>(context, blockEntity, partialTick, Direction.SOUTH, Direction.UP, Models.partial(OccultEngineeringPartialModels.TOP_SHAFT));
                    })
                    .validBlocks(OccultEngineeringBlocks.MECHANICAL_CHAMBER)
                    .renderer(() -> MechanicalChamberRenderer::new)
                    .register();

    public static void register() {
    }
}
