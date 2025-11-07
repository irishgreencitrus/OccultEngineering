package io.github.irishgreencitrus.occultengineering.registry;

import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import io.github.irishgreencitrus.occultengineering.compat.Mods;
import io.github.irishgreencitrus.occultengineering.compat.enchant_industry.DyeingItemRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.Nullable;

public class OccultEngineeringRecipeTypes {
    public static final DeferredRegister<RecipeType<?>> REGISTER_RECIPE_TYPE = DeferredRegister.create(Registries.RECIPE_TYPE, OccultEngineering.MODID);
    public static final DeferredRegister<RecipeSerializer<?>> REGISTER_RECIPES = DeferredRegister.create(Registries.RECIPE_SERIALIZER, OccultEngineering.MODID);

    public static final @Nullable DeferredHolder<RecipeSerializer<?>, RecipeSerializer<DyeingItemRecipe>> DYEING_ITEM;
    public static final @Nullable DeferredHolder<RecipeType<?>, RecipeType<DyeingItemRecipe>> DYEING_ITEM_TYPE;

    static {
        if (Mods.ENCHANTMENT_INDUSTRY.isLoaded()) {
            DYEING_ITEM = REGISTER_RECIPES.register("dyeing_item", () -> DyeingItemRecipe.SERIALIZER);
            DYEING_ITEM_TYPE = registerRecipeType("dyeing_item");
        } else {
            DYEING_ITEM = null;
            DYEING_ITEM_TYPE = null;
        }
    }

    public static void register(IEventBus modEventBus) {
        REGISTER_RECIPE_TYPE.register(modEventBus);
        REGISTER_RECIPES.register(modEventBus);
    }

    static <T extends Recipe<?>> DeferredHolder<RecipeType<?>, RecipeType<T>> registerRecipeType(final String id) {
        return REGISTER_RECIPE_TYPE.register(id, () -> new RecipeType<T>() {
            public String toString() {
                return id;
            }
        });
    }
}
