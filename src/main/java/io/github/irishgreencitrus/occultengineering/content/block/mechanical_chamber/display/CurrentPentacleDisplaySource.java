package io.github.irishgreencitrus.occultengineering.content.block.mechanical_chamber.display;

import com.simibubi.create.content.redstone.displayLink.DisplayLinkContext;
import com.simibubi.create.content.redstone.displayLink.source.SingleLineDisplaySource;
import com.simibubi.create.content.redstone.displayLink.target.DisplayTargetStats;
import io.github.irishgreencitrus.occultengineering.content.block.mechanical_chamber.MechanicalChamberBlockEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class CurrentPentacleDisplaySource extends SingleLineDisplaySource {
    @Override
    protected MutableComponent provideLine(DisplayLinkContext context, DisplayTargetStats stats) {
        var blockEntity = context.getSourceBlockEntity();
        if (!(blockEntity instanceof MechanicalChamberBlockEntity chamber)) {
            return EMPTY_LINE;
        }

        var recipe = chamber.getCurrentRitualRecipe();
        if (recipe == null) {
            return Component.literal("No ritual active");
        }

        return MechanicalChamberBlockEntity.getPentacleName(recipe.getPentacleId()).copy();
    }

    @Override
    protected boolean allowsLabeling(DisplayLinkContext context) {
        return true;
    }
}
