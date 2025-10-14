package io.github.irishgreencitrus.occultengineering.compat.jei;

import com.klikli_dev.occultism.crafting.recipe.SpiritFireRecipe;
import com.klikli_dev.occultism.integration.jei.impl.JeiRecipeTypes;
import com.klikli_dev.occultism.registry.OccultismBlocks;
import com.klikli_dev.occultism.registry.OccultismRecipes;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import io.github.irishgreencitrus.occultengineering.compat.jei.category.FanEnspiritCategory;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringBlocks;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringFluids;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@mezz.jei.api.JeiPlugin
public class JeiPlugin implements IModPlugin {
    protected static IJeiRuntime runtime;
    private final List<CreateRecipeCategory<?>> allCategories = new ArrayList<>();

    public static IJeiRuntime getJeiRuntime() {
        return runtime;
    }

    public void loadCategories() {
        allCategories.clear();
        // FIXME: why doesn't this appear in JEI? Check Create's implementation first.
        allCategories.add(
                builder(SpiritFireRecipe.class)
                        .addTypedRecipes(OccultismRecipes.SPIRIT_FIRE_TYPE::get)
                        .catalystStack(getOcEngFan("fan_enspirit"))
                        .doubleItemIcon(AllItems.PROPELLER.get(), OccultismBlocks.SPIRIT_CAMPFIRE.get())
                        .emptyBackground(178, 72)
                        .build("fan_enspirit", FanEnspiritCategory::new)
        );
    }

    private static Supplier<ItemStack> getOcEngFan(String name) {
        var stack = AllBlocks.ENCASED_FAN.asStack();
        stack.set(DataComponents.CUSTOM_NAME, OccultEngineering.lang().translate(name+".fan").component().withStyle((s) -> s.withItalic(false)));
        return () -> stack;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        loadCategories();
        registration.addRecipeCategories(allCategories.toArray(IRecipeCategory[]::new));
    }

    private <T extends Recipe<?>> CategoryBuilder<T> builder(Class<T> cls) {
        return new CategoryBuilder<>(cls);
    }

    private class CategoryBuilder<T extends Recipe<?>> extends CreateRecipeCategory.Builder<T> {
        public CategoryBuilder(Class<? extends T> recipeClass) {
            super(recipeClass);
        }

        @Override
        public CreateRecipeCategory<T> build(ResourceLocation id, CreateRecipeCategory.Factory<T> factory) {
            CreateRecipeCategory<T> category = super.build(id, factory);
            allCategories.add(category);
            return category;
        }
    }

    @Override
    public void onRuntimeAvailable(@NotNull IJeiRuntime jeiRuntime) {
        JeiPlugin.runtime = jeiRuntime;
        List<FluidStack> fluidIngredients = new ArrayList<>();
        fluidIngredients.add(new FluidStack(OccultEngineeringFluids.SPIRIT_SOLUTION.get(), FluidType.BUCKET_VOLUME));

        jeiRuntime.getIngredientManager().addIngredientsAtRuntime(NeoForgeTypes.FLUID_STACK, fluidIngredients);
    }

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return OccultEngineering.asResource("jei");
    }

    @Override
    public void registerRecipeCatalysts(@NotNull IRecipeCatalystRegistration registration) {
        allCategories.forEach(c -> c.registerCatalysts(registration));
        registration.addRecipeCatalyst(new ItemStack(OccultEngineeringBlocks.MECHANICAL_CHAMBER), JeiRecipeTypes.RITUAL);
        registration.addRecipeCatalyst(new ItemStack(OccultEngineeringBlocks.MECHANICAL_PULVERIZER), JeiRecipeTypes.CRUSHING);
    }

    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registration) {
        allCategories.forEach(c -> c.registerRecipes(registration));
    }
}
