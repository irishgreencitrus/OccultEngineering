package io.github.irishgreencitrus.occultengineering.content.block.mechanical_chamber.display;

import com.simibubi.create.content.redstone.displayLink.DisplayLinkContext;
import com.simibubi.create.content.redstone.displayLink.source.PercentOrProgressBarDisplaySource;
import com.simibubi.create.foundation.gui.ModularGuiLineBuilder;
import com.simibubi.create.foundation.utility.CreateLang;
import io.github.irishgreencitrus.occultengineering.content.block.mechanical_chamber.MechanicalChamberBlockEntity;
import io.github.irishgreencitrus.occultengineering.content.block.mechanical_chamber.RitualProcessorBehaviour;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

public class RitualCompletionDisplaySource extends PercentOrProgressBarDisplaySource {
    @Override
    protected @Nullable Float getProgress(DisplayLinkContext context) {
        var be = context.getSourceBlockEntity();
        if (!(be instanceof MechanicalChamberBlockEntity mcb)) {
            return 0f;
        }

        var ritualBehaviour = mcb.getBehaviour(RitualProcessorBehaviour.TYPE);
        if (ritualBehaviour == null) {
            return 0f;
        }
        if (!ritualBehaviour.isRitualActive()) {
            return 0f;
        }
        var recipe = ritualBehaviour.getRitualRecipe();
        return recipe.map(
                ritualRecipeRecipeHolder -> ritualBehaviour.currentTime /
                        (float) ritualRecipeRecipeHolder.value().getDuration())
                .orElse(0f);
    }

    @Override
    protected boolean progressBarActive(DisplayLinkContext context) {
        return context.sourceConfig().getInt("Mode") == 1;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void initConfigurationWidgets(DisplayLinkContext context, ModularGuiLineBuilder builder, boolean isFirstLine) {
        super.initConfigurationWidgets(context, builder, isFirstLine);
        if (isFirstLine)
            return;
        builder.addSelectionScrollInput(0, 120,
                (si, l) -> si.forOptions(CreateLang.translatedOptions("display_source.fill_level", "percent", "progress_bar"))
                        .titled(CreateLang.translateDirect("display_source.fill_level.display")),
                "Mode");
    }

    @Override
    protected boolean allowsLabeling(DisplayLinkContext context) {
        return true;
    }
}
