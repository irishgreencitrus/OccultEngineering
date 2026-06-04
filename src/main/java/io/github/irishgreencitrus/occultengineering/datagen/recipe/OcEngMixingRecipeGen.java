package io.github.irishgreencitrus.occultengineering.datagen.recipe;

import com.klikli_dev.occultism.registry.OccultismItems;
import com.simibubi.create.api.data.recipe.MixingRecipeGen;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringFluids;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static io.github.irishgreencitrus.occultengineering.datagen.recipe.OcEngRecipeProvider.I.copperDust;
import static io.github.irishgreencitrus.occultengineering.datagen.recipe.OcEngRecipeProvider.I.silverDust;

@SuppressWarnings("unused")
public class OcEngMixingRecipeGen extends MixingRecipeGen {
    public OcEngMixingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, OccultEngineering.MODID);
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
                            .output(OccultEngineeringItems.STERLING_SILVER_INGOT)),

            SILVER_PHLOGISTATE = create(OccultEngineering.asResource("silver_phlogistate"), b ->
                    b.require(OccultEngineeringItems.STERLING_SILVER_INGOT)
                            .require(OccultEngineeringItems.PHLOGISTON)
                            .output(OccultEngineeringItems.SILVER_PHLOGISTATE));

    private GeneratedRecipe bookOfBindingStandard(ResourceLocation loc, ItemLike outputBook, ItemLike... dyes) {
        bookOfBindingFromRaw(loc, outputBook, null, dyes);
        return bookOfBindingFromEmptyBook(ResourceLocation.fromNamespaceAndPath(loc.getNamespace(), loc.getPath() + "_from_empty"), outputBook, null, dyes);
    }

    private GeneratedRecipe bookOfBindingSpirit(ResourceLocation loc, ItemLike outputBook, int spiritSolutionMb, ItemLike... dyes) {
        var fluidIngredient = SizedFluidIngredient.of(OccultEngineeringFluids.SPIRIT_SOLUTION.getSource(), spiritSolutionMb);
        bookOfBindingFromRaw(loc, outputBook, fluidIngredient, dyes);
        return bookOfBindingFromEmptyBook(ResourceLocation.fromNamespaceAndPath(loc.getNamespace(), loc.getPath() + "_from_empty"), outputBook, fluidIngredient, dyes);
    }

    private GeneratedRecipe bookOfBindingFromRaw(ResourceLocation loc, ItemLike outputBook, @Nullable SizedFluidIngredient fluidIngredient, ItemLike... dyes) {
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

    private GeneratedRecipe bookOfBindingFromEmptyBook(ResourceLocation loc, ItemLike outputBook, @Nullable SizedFluidIngredient fluidIngredient, ItemLike... dyes) {
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
}
