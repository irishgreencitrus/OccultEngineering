package io.github.irishgreencitrus.occultengineering.datagen.recipe;

import com.klikli_dev.occultism.registry.OccultismItems;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.foundation.data.recipe.ProcessingRecipeGen;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import io.github.irishgreencitrus.occultengineering.compat.Mods;
import io.github.irishgreencitrus.occultengineering.datagen.JsonDatagenIngredient;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringBlocks;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;

@SuppressWarnings("unused")
public class OcEngItemApplicationRecipeGen extends ProcessingRecipeGen {
    GeneratedRecipe ENSPIRIT_CATALYST = create(OccultEngineering.asResource("enspirit_catalyst_from_empty"), b ->
            b.require(new JsonDatagenIngredient(Mods.CREATE_CONNECTED, "empty_fan_catalyst"))
                    .require(OccultismItems.DATURA.get())
                    .withCondition(new ModLoadedCondition(Mods.CREATE_CONNECTED.id))
                    .output(OccultEngineeringBlocks.FAN_ENSPIRIT_CATALYST)
    );

    public OcEngItemApplicationRecipeGen(PackOutput generator) {
        super(generator);
    }

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return AllRecipeTypes.ITEM_APPLICATION;
    }
}
