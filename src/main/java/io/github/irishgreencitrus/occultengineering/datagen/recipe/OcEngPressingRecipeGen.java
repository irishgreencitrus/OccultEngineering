package io.github.irishgreencitrus.occultengineering.datagen.recipe;

import com.klikli_dev.occultism.registry.OccultismBlocks;
import com.simibubi.create.api.data.recipe.PressingRecipeGen;
import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringItems;
import net.minecraft.data.PackOutput;

@SuppressWarnings("unused")
public class OcEngPressingRecipeGen extends PressingRecipeGen {
    GeneratedRecipe MECHANICAL_UPGRADE_EMPTY = create(
            OccultEngineering.asResource("mechanical_upgrade_empty"),
            builder -> builder.require(OccultismBlocks.OTHERSTONE.get())
                    .output(0.05F, OccultEngineeringItems.MECHANICAL_UPGRADE_EMPTY));

    public OcEngPressingRecipeGen(PackOutput output) {
        super(output, OccultEngineering.MODID);
    }
}
