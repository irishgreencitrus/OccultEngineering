package io.github.irishgreencitrus.occultengineering.datagen.recipe;

import com.klikli_dev.occultism.registry.OccultismItems;
import com.simibubi.create.api.data.recipe.ItemApplicationRecipeGen;
import com.simibubi.create.foundation.data.SimpleDatagenIngredient;
import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import io.github.irishgreencitrus.occultengineering.compat.Mods;
import io.github.irishgreencitrus.occultengineering.datagen.JsonDatagenIngredient;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
public class OcEngItemApplicationRecipeGen extends ItemApplicationRecipeGen {
    GeneratedRecipe ENSPIRIT_CATALYST = create(OccultEngineering.asResource("enspirit_catalyst_from_empty"), b ->
            b.require(new JsonDatagenIngredient(Mods.CREATE_CONNECTED, "empty_fan_catalyst"))
                    .require(OccultismItems.DATURA.get())
                    .withCondition(new ModLoadedCondition(Mods.CREATE_CONNECTED.id))
                    .output(OccultEngineeringBlocks.FAN_ENSPIRIT_CATALYST)
    );

    public OcEngItemApplicationRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, OccultEngineering.MODID);
    }
}
