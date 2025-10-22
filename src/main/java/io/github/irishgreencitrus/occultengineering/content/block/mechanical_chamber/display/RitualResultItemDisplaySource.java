package io.github.irishgreencitrus.occultengineering.content.block.mechanical_chamber.display;

import com.simibubi.create.content.redstone.displayLink.DisplayLinkContext;
import com.simibubi.create.content.redstone.displayLink.source.SingleLineDisplaySource;
import com.simibubi.create.content.redstone.displayLink.target.DisplayTargetStats;
import io.github.irishgreencitrus.occultengineering.content.block.mechanical_chamber.MechanicalChamberBlockEntity;
import io.github.irishgreencitrus.occultengineering.content.block.mechanical_chamber.RitualProcessorBehaviour;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class RitualResultItemDisplaySource extends SingleLineDisplaySource {
    @Override
    protected MutableComponent provideLine(DisplayLinkContext context, DisplayTargetStats stats) {
        var be = context.getSourceBlockEntity();
        if (!(be instanceof MechanicalChamberBlockEntity mcb)) {
            return EMPTY_LINE;
        }

        var ritualBehaviour = mcb.getBehaviour(RitualProcessorBehaviour.TYPE);
        if (ritualBehaviour == null) {
            return EMPTY_LINE;
        }

        var recipe = ritualBehaviour.currentRitualRecipe;
        if (recipe == null) {
            return Component.literal("No ritual active");
        }

        // You can pass null here, I promise.
        return recipe.value().getResultItem(null).getHoverName().copy();
    }

    @Override
    protected boolean allowsLabeling(DisplayLinkContext context) {
        return true;
    }
}
