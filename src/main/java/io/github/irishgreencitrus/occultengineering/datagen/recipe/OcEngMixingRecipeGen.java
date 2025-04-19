package io.github.irishgreencitrus.occultengineering.datagen.recipe;

import com.klikli_dev.occultism.registry.OccultismItems;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.foundation.data.recipe.ProcessingRecipeGen;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringFluids;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;

import static io.github.irishgreencitrus.occultengineering.datagen.recipe.OcEngRecipeProvider.I.copperDust;
import static io.github.irishgreencitrus.occultengineering.datagen.recipe.OcEngRecipeProvider.I.silverDust;

@SuppressWarnings("unused")
public class OcEngMixingRecipeGen extends ProcessingRecipeGen {
    public OcEngMixingRecipeGen(PackOutput output) {
        super(output);
    }

    GeneratedRecipe

            MIX_FOLIOT =
            bookOfBindingStandard(
                    OccultEngineering.asResource("book_of_binding_foliot"),
                    OccultismItems.BOOK_OF_BINDING_FOLIOT.get(), Items.WHITE_DYE),
            MIX_DJINNI =
                    bookOfBindingStandard(
                            OccultEngineering.asResource("book_of_binding_djinni"),
                            OccultismItems.BOOK_OF_BINDING_DJINNI.get(), Items.GREEN_DYE),
            MIX_AFRIT =
                    bookOfBindingStandard(
                            OccultEngineering.asResource("book_of_binding_afrit"),
                            OccultismItems.BOOK_OF_BINDING_AFRIT.get(), Items.PURPLE_DYE),
            MIX_MARID =
                    bookOfBindingStandard(
                            OccultEngineering.asResource("book_of_binding_marid"),
                            OccultismItems.BOOK_OF_BINDING_MARID.get(), Items.ORANGE_DYE, Items.PURPLE_DYE),
            MIX_PUCA =
                    bookOfBindingSpirit(
                            OccultEngineering.asResource("book_of_binding_puca"),
                            OccultEngineeringItems.BOOK_OF_BINDING_PUCA, 50, Items.RED_DYE),

    SPIRIT_SOLUTION_FROM_FRUIT = create(OccultEngineering.asResource("spirit_solution_from_fruit"), b ->
            b.require(OccultismItems.DATURA::get)
                    .require(Fluids.WATER, 100)
                    .requiresHeat(HeatCondition.HEATED)
                    .output(OccultEngineeringFluids.SPIRIT_SOLUTION.get(), 100)),

    SPIRIT_SOLUTION_FROM_ESSENCE = create(OccultEngineering.asResource("spirit_solution_from_essence"), b ->
            b.require(OccultismItems.OTHERWORLD_ESSENCE::get)
                    .require(Fluids.WATER, 1000)
                    .requiresHeat(HeatCondition.HEATED)
                    .output(OccultEngineeringFluids.SPIRIT_SOLUTION.get(), 1000)),

    STERLING_SILVER_INGOT = create(OccultEngineering.asResource("sterling_silver_ingot"), b ->
            b.require(copperDust())
                    .require(silverDust())
                    .requiresHeat(HeatCondition.HEATED)
                    .output(OccultEngineeringItems.STERLING_SILVER_INGOT));

    private GeneratedRecipe bookOfBindingStandard(ResourceLocation loc, ItemLike outputBook, ItemLike... dyes) {
        bookOfBindingFromRaw(loc, outputBook, null, dyes);
        return bookOfBindingFromEmptyBook(new ResourceLocation(loc.getNamespace(), loc.getPath() + "_from_empty"), outputBook, null, dyes);
    }

    private GeneratedRecipe bookOfBindingSpirit(ResourceLocation loc, ItemLike outputBook, int spiritSolutionMb, ItemLike... dyes) {
        var fluidIngredient = FluidIngredient.fromFluid(OccultEngineeringFluids.SPIRIT_SOLUTION.get(), spiritSolutionMb);
        bookOfBindingFromRaw(loc, outputBook, fluidIngredient, dyes);
        return bookOfBindingFromEmptyBook(new ResourceLocation(loc.getNamespace(), loc.getPath() + "_from_empty"), outputBook, fluidIngredient, dyes);
    }

    private GeneratedRecipe bookOfBindingFromRaw(ResourceLocation loc, ItemLike outputBook, @Nullable FluidIngredient fluidIngredient, ItemLike... dyes) {
        return create(loc, b -> {
            var recipe = b.require(OccultismItems.TABOO_BOOK::get)
                    .require(OccultismItems.PURIFIED_INK::get)
                    .require(OccultismItems.AWAKENED_FEATHER::get)
                    .output(outputBook);
            for (ItemLike dye : dyes) {
                recipe = recipe.require(dye);
            }
            if (fluidIngredient != null) {
                recipe = recipe.require(fluidIngredient);
            }
            return recipe;
        });
    }

    private GeneratedRecipe bookOfBindingFromEmptyBook(ResourceLocation loc, ItemLike outputBook, @Nullable FluidIngredient fluidIngredient, ItemLike... dyes) {
        return create(loc, b -> {
            var recipe = b.require(OccultismItems.BOOK_OF_BINDING_EMPTY::get)
                    .output(outputBook);
            for (ItemLike dye : dyes) {
                recipe = recipe.require(dye);
            }
            if (fluidIngredient != null) {
                recipe = recipe.require(fluidIngredient);
            }
            return recipe;
        });
    }

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return AllRecipeTypes.MIXING;
    }
}
