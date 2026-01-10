package io.github.irishgreencitrus.occultengineering.datagen.recipe;

import com.klikli_dev.occultism.registry.OccultismBlocks;
import com.simibubi.create.api.data.recipe.PressingRecipeGen;
import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
public class OcEngPressingRecipeGen extends PressingRecipeGen {
    GeneratedRecipe MECHANICAL_UPGRADE_EMPTY = create("mechanical_upgrade_empty",
            b -> b.require(OccultismBlocks.OTHERROCK)
                    .output(0.05F, OccultEngineeringItems.MECHANICAL_UPGRADE_EMPTY));

    public OcEngPressingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, OccultEngineering.MODID);
    }
}
