package io.github.irishgreencitrus.occultengineering.datagen.recipe;

import com.klikli_dev.occultism.registry.OccultismItems;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.foundation.data.recipe.CreateRecipeProvider;
import com.simibubi.create.foundation.data.recipe.ProcessingRecipeGen;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringFluids;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringItems;
import net.minecraft.data.PackOutput;

@SuppressWarnings("unused")
public class OcEngFillingRecipeGen extends ProcessingRecipeGen {
    CreateRecipeProvider.GeneratedRecipe
            BIND_FOLIOT_BOOK = create(OccultEngineering.asResource("bind_foliot_book"), b ->
            b.require(OccultismItems.BOOK_OF_BINDING_FOLIOT::get)
                    .require(OccultEngineeringFluids.SPIRIT_SOLUTION.get(), 25)
                    .output(OccultismItems.BOOK_OF_BINDING_BOUND_FOLIOT::get)),

    BIND_DJINNI_BOOK = create(OccultEngineering.asResource("bind_djinni_book"), b ->
            b.require(OccultismItems.BOOK_OF_BINDING_DJINNI::get)
                    .require(OccultEngineeringFluids.SPIRIT_SOLUTION.get(), 25)
                    .output(OccultismItems.BOOK_OF_BINDING_BOUND_DJINNI::get)),

    BIND_AFRIT_BOOK = create(OccultEngineering.asResource("bind_afrit_book"), b ->
            b.require(OccultismItems.BOOK_OF_BINDING_AFRIT::get)
                    .require(OccultEngineeringFluids.SPIRIT_SOLUTION.get(), 25)
                    .output(OccultismItems.BOOK_OF_BINDING_BOUND_AFRIT::get)),

    BIND_MARID_BOOK = create(OccultEngineering.asResource("bind_marid_book"), b ->
            b.require(OccultismItems.BOOK_OF_BINDING_MARID::get)
                    .require(OccultEngineeringFluids.SPIRIT_SOLUTION.get(), 25)
                    .output(OccultismItems.BOOK_OF_BINDING_BOUND_MARID::get)),

    BIND_PUCA_BOOK = create(OccultEngineering.asResource("bind_puca_book"), b ->
            b.require(OccultEngineeringItems.BOOK_OF_BINDING_PUCA)
                    .require(OccultEngineeringFluids.SPIRIT_SOLUTION.get(), 25)
                    .output(OccultEngineeringItems.BOOK_OF_BINDING_BOUND_PUCA));

    public OcEngFillingRecipeGen(PackOutput generator) {
        super(generator);
    }

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return AllRecipeTypes.FILLING;
    }
}
