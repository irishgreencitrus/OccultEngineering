package io.github.irishgreencitrus.occultengineering.content.block.mechanical_chamber.display;

import com.simibubi.create.content.redstone.displayLink.DisplayLinkContext;
import com.simibubi.create.content.redstone.displayLink.source.PercentOrProgressBarDisplaySource;
import com.simibubi.create.foundation.gui.ModularGuiLineBuilder;
import com.simibubi.create.foundation.utility.CreateLang;
import io.github.irishgreencitrus.occultengineering.content.block.mechanical_chamber.MechanicalChamberBlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

public class RitualCompletionDisplaySource extends PercentOrProgressBarDisplaySource {
    @Override
    protected @Nullable Float getProgress(DisplayLinkContext context) {
        var blockEntity = context.getSourceBlockEntity();
        if (!(blockEntity instanceof MechanicalChamberBlockEntity chamber)) {
            return 0f;
        }

        var recipe = chamber.getCurrentRitualRecipe();
        if (recipe == null) {
            return 0f;
        }

        return chamber.currentTime / (float) recipe.getDuration();
    }

    @Override
    protected boolean progressBarActive(DisplayLinkContext context) {
        return context.sourceConfig().getInt("Mode") == 1;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void initConfigurationWidgets(DisplayLinkContext context, ModularGuiLineBuilder builder, boolean isFirstLine) {
        super.initConfigurationWidgets(context, builder, isFirstLine);
        if (isFirstLine) {
            return;
        }
        builder.addSelectionScrollInput(0, 120,
                (input, label) -> input.forOptions(CreateLang.translatedOptions(
                                "display_source.fill_level", "percent", "progress_bar"))
                        .titled(CreateLang.translateDirect("display_source.fill_level.display")),
                "Mode");
    }

    @Override
    protected boolean allowsLabeling(DisplayLinkContext context) {
        return true;
    }
}
