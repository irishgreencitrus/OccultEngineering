package io.github.irishgreencitrus.occultengineering.datagen.recipe;

import com.simibubi.create.AllTags;
import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class OcEngRecipeProvider extends RecipeProvider {
    protected final List<OcEngRecipeProvider.GeneratedRecipe> all = new ArrayList<>();

    public OcEngRecipeProvider(PackOutput output) {
        super(output);
    }

    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> finishedRecipeConsumer) {
        this.all.forEach((c) -> c.register(finishedRecipeConsumer));
        var logger = OccultEngineering.LOGGER;
        var name = OccultEngineering.NAME;
        logger.info("{} registered {} recipe{}", name, this.all.size(), this.all.size() == 1 ? "" : "s");
    }

    protected OcEngRecipeProvider.GeneratedRecipe register(OcEngRecipeProvider.GeneratedRecipe recipe) {
        this.all.add(recipe);
        return recipe;
    }

    protected static class Marker {
        protected Marker() {
        }
    }

    protected static class I {
        protected I() {
        }

        static TagKey<Item> copper() {
            return Tags.Items.INGOTS_COPPER;
        }

        static TagKey<Item> silverDust() {
            return AllTags.forgeItemTag("dusts/silver");
        }

        static TagKey<Item> copperDust() {
            return AllTags.forgeItemTag("dusts/copper");
        }

        static TagKey<Item> zincDust() {
            return AllTags.forgeItemTag("dusts/zinc");
        }

        static TagKey<Item> brassDust() {
            return AllTags.forgeItemTag("dusts/brass");
        }

        static TagKey<Item> sterlingNugget() {
            return AllTags.forgeItemTag("nuggets/sterling_silver");
        }

        static TagKey<Item> sterlingIngot() {
            return AllTags.forgeItemTag("ingots/sterling_silver");
        }

        static TagKey<Item> sterlingBlock() {
            return AllTags.forgeItemTag("storage_blocks/sterling_silver");
        }
    }

    @FunctionalInterface
    public interface GeneratedRecipe {
        void register(Consumer<FinishedRecipe> var1);
    }

}
