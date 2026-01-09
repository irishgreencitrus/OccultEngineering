package io.github.irishgreencitrus.occultengineering.registry;

import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import io.github.irishgreencitrus.occultengineering.content.recipe.UpgradeTierRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class OccultEngineeringRecipes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(
            BuiltInRegistries.RECIPE_TYPE, OccultEngineering.MODID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPES = DeferredRegister.create(
            BuiltInRegistries.RECIPE_SERIALIZER, OccultEngineering.MODID);

    public static final DeferredHolder<RecipeType<?>, RecipeType<UpgradeTierRecipe>> UPGRADE_TIER_TYPE = registerRecipeType("upgrade_tier");
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<UpgradeTierRecipe>> UPGRADE_TIER = RECIPES.register("upgrade_tier", () -> new SimpleCraftingRecipeSerializer<>(UpgradeTierRecipe::new));

    static <T extends Recipe<?>> DeferredHolder<RecipeType<?>, RecipeType<T>> registerRecipeType(final String id) {
        return RECIPE_TYPES.register(id, () -> new RecipeType<T>() {
            public String toString() {
                return id;
            }
        });
    }

    public static void register(IEventBus modEventBus) {
        RECIPE_TYPES.register(modEventBus);
        RECIPES.register(modEventBus);
    }
}
