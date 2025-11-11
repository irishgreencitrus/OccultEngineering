package io.github.irishgreencitrus.occultengineering.datagen.recipe;

import com.simibubi.create.api.data.recipe.ProcessingRecipeGen;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.Tags;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class OcEngRecipeProvider extends RecipeProvider {
    static final List<ProcessingRecipeGen<?,?,?>> GENERATORS = new ArrayList<>();

    public OcEngRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
    }

    public static void registerAllProcessing(DataGenerator gen, PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        GENERATORS.add(new OcEngCompactingRecipeGen(output, registries));
        GENERATORS.add(new OcEngMixingRecipeGen(output, registries));
        GENERATORS.add(new OcEngFillingRecipeGen(output, registries));
        GENERATORS.add(new OcEngItemApplicationRecipeGen(output, registries));
        gen.addProvider(true, new DataProvider() {
            @Override
            public CompletableFuture<?> run(CachedOutput cachedOutput) {
                return CompletableFuture
                        .allOf(GENERATORS.stream().map(gen -> gen.run(cachedOutput))
                                .toArray(CompletableFuture[]::new));
            }

            @Override
            public String getName() {
                return "Occult Engineering's Processing Recipes";
            }
        });
    }

    protected static class I {
        protected I() {
        }

        static <T> TagKey<T> tag(Registry<T> registry, ResourceLocation id) {
            return TagKey.create(registry.key(), id);
        }

        static TagKey<Item> commonItemTag(String name) {
            return tag(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath("c", name));
        }

        static TagKey<Item> copper() {
            return Tags.Items.INGOTS_COPPER;
        }

        static TagKey<Item> silverDust() {
            return commonItemTag("dusts/silver");
        }

        static TagKey<Item> copperDust() {
            return commonItemTag("dusts/copper");
        }

        static TagKey<Item> zincDust() {
            return commonItemTag("dusts/zinc");
        }

        static TagKey<Item> brassDust() {
            return commonItemTag("dusts/brass");
        }

        static TagKey<Item> sterlingNugget() {
            return commonItemTag("nuggets/sterling_silver");
        }

        static TagKey<Item> sterlingIngot() {
            return commonItemTag("ingots/sterling_silver");
        }

        static TagKey<Item> sterlingBlock() {
            return commonItemTag("storage_blocks/sterling_silver");
        }
    }
}
