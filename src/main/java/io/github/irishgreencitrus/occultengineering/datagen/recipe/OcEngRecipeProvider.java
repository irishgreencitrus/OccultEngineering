package io.github.irishgreencitrus.occultengineering.datagen.recipe;

import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
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


    }

    @FunctionalInterface
    public interface GeneratedRecipe {
        void register(Consumer<FinishedRecipe> var1);
    }

}
