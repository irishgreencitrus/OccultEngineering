package io.github.irishgreencitrus.occultengineering.compat.jei;

import com.klikli_dev.occultism.crafting.recipe.SpiritFireRecipe;
import com.klikli_dev.occultism.registry.OccultismBlocks;
import com.klikli_dev.occultism.registry.OccultismRecipes;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import io.github.irishgreencitrus.occultengineering.compat.jei.category.FanEnspiritCategory;
import io.github.irishgreencitrus.occultengineering.compat.jei.category.RecipeCategoryBuilder;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringFluids;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@mezz.jei.api.JeiPlugin
public class JeiPlugin implements IModPlugin {
    protected static IJeiRuntime runtime;
    private final List<CreateRecipeCategory<?>> allCategories = new ArrayList<>();

    public static IJeiRuntime getJeiRuntime() {
        return runtime;
    }

    public void loadCategories() {
        allCategories.clear();
        allCategories.add(
                builder(SpiritFireRecipe.class)
                        .addTypedRecipes(OccultismRecipes.SPIRIT_FIRE_TYPE::get)
                        .catalystStack(
                                () -> AllBlocks.ENCASED_FAN.asStack()
                                        .setHoverName(
                                                OccultEngineering
                                                        .LANG
                                                        .translate("fan_enspirit.fan")
                                                        .component()
                                                        .withStyle(
                                                                (style) -> style.withItalic(false))))
                        .doubleItemIcon(AllItems.PROPELLER.get(), OccultismBlocks.SPIRIT_CAMPFIRE.get())
                        .emptyBackground(178, 72)
                        .build("fan_enspirit", FanEnspiritCategory::new)
        );
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        loadCategories();
        registration.addRecipeCategories(allCategories.toArray(IRecipeCategory[]::new));
    }

    private static <T extends Recipe<?>> RecipeCategoryBuilder<T> builder(Class<T> cls) {
        return new RecipeCategoryBuilder<>(OccultEngineering.MODID, cls);
    }

    @Override
    public void onRuntimeAvailable(@NotNull IJeiRuntime jeiRuntime) {
        JeiPlugin.runtime = jeiRuntime;
        List<FluidStack> fluidIngredients = new ArrayList<>();
        fluidIngredients.add(new FluidStack(OccultEngineeringFluids.SPIRIT_SOLUTION.get().getSource(), FluidType.BUCKET_VOLUME));

        jeiRuntime.getIngredientManager().addIngredientsAtRuntime(ForgeTypes.FLUID_STACK, fluidIngredients);
    }

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(OccultEngineering.MODID, "jei");
    }

    @Override
    public void registerRecipeCatalysts(@NotNull IRecipeCatalystRegistration registration) {
        allCategories.forEach(c -> c.registerCatalysts(registration));
    }

    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registration) {
        allCategories.forEach(c -> c.registerRecipes(registration));
    }
}
