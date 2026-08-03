package io.github.irishgreencitrus.occultengineering.registry;

import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import io.github.irishgreencitrus.occultengineering.content.recipe.UpgradeTierRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class OccultEngineeringRecipes {
    private static final DeferredRegister<RecipeSerializer<?>> RECIPES =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, OccultEngineering.MODID);

    public static final RegistryObject<RecipeSerializer<UpgradeTierRecipe>> UPGRADE_TIER =
            RECIPES.register("upgrade_tier", () -> new SimpleCraftingRecipeSerializer<>(UpgradeTierRecipe::new));

    private OccultEngineeringRecipes() {
    }

    public static void register(IEventBus modEventBus) {
        RECIPES.register(modEventBus);
    }
}
