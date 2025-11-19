package io.github.irishgreencitrus.occultengineering.datagen.recipe;

import com.klikli_dev.occultism.registry.OccultismItems;
import com.simibubi.create.api.data.recipe.CompactingRecipeGen;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringFluids;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;

import static io.github.irishgreencitrus.occultengineering.datagen.recipe.OcEngRecipeProvider.I.*;

@SuppressWarnings("unused")
public class OcEngCompactingRecipeGen extends CompactingRecipeGen {
    GeneratedRecipe
            IMPURE_WHITE_CHALK = create(OccultEngineering.asResource("chalk_white_impure"), b ->
            b.require(OccultismItems.BURNT_OTHERSTONE::get)
                    .require(OccultismItems.BURNT_OTHERSTONE::get)
                    .require(OccultismItems.OTHERWORLD_ASHES::get)
                    .require(OccultismItems.OTHERWORLD_ASHES::get)
                    .output(OccultismItems.CHALK_WHITE_IMPURE::get)),

    IMPURE_COPPER_CHALK = create(OccultEngineering.asResource("chalk_copper_impure"), b ->
            b.require(copperDust())
                    .require(copperDust())
                    .require(OccultismItems.CHALK_WHITE_IMPURE::get)
                    .require(OccultEngineeringFluids.SPIRIT_SOLUTION.get(), 100)
                    .output(OccultEngineeringItems.COPPER_CHALK_IMPURE)),

    IMPURE_ZINC_CHALK = create(OccultEngineering.asResource("chalk_zinc_impure"), b ->
            b.require(zincDust())
                    .require(zincDust())
                    .require(OccultismItems.CHALK_WHITE_IMPURE::get)
                    .require(OccultEngineeringFluids.SPIRIT_SOLUTION.get(), 250)
                    .output(OccultEngineeringItems.ZINC_CHALK_IMPURE)),

    IMPURE_BRASS_CHALK = create(OccultEngineering.asResource("chalk_brass_impure"), b ->
            b.require(brassDust())
                    .require(brassDust())
                    .require(OccultismItems.CHALK_WHITE_IMPURE::get)
                    .require(OccultEngineeringFluids.SPIRIT_SOLUTION.get(), 500)
                    .output(OccultEngineeringItems.BRASS_CHALK_IMPURE)),

    SPIRIT_SOLUTION_FROM_SEEDS = create(OccultEngineering.asResource("spirit_solution_from_seeds"), b ->
            b.require(OccultismItems.DATURA_SEEDS::get)
                    .output(OccultEngineeringFluids.SPIRIT_SOLUTION.get(), 10)),

    PHLOGISTON = create(OccultEngineering.asResource("phlogiston"), b ->
            b.require(Fluids.LAVA, 500)
                    .require(Items.BLAZE_POWDER)
                    .require(Items.BLAZE_POWDER)
                    .require(Items.BLAZE_POWDER)
                    .require(Items.BLAZE_POWDER)
                    .requiresHeat(HeatCondition.SUPERHEATED)
                    .output(OccultEngineeringItems.PHLOGISTON));

    public OcEngCompactingRecipeGen(PackOutput generator) {
        super(generator, OccultEngineering.MODID);
    }
}
