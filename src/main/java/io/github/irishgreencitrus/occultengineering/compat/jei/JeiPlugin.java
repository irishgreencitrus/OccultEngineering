package io.github.irishgreencitrus.occultengineering.compat.jei;

import com.klikli_dev.occultism.integration.jei.JeiRecipeTypes;
import com.simibubi.create.AllBlocks;
import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringFluids;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@mezz.jei.api.JeiPlugin
public class JeiPlugin implements IModPlugin {
    protected static IJeiRuntime runtime;

    public static IJeiRuntime getJeiRuntime() {
        return runtime;
    }
    @Override
    public void onRuntimeAvailable(@NotNull IJeiRuntime jeiRuntime) {
        JeiPlugin.runtime = jeiRuntime;
        List<FluidStack> fluidIngredients = new ArrayList<>();
        fluidIngredients.add(
                new FluidStack(OccultEngineeringFluids.SPIRIT_SOLUTION.get().getSource(), FluidType.BUCKET_VOLUME));
        runtime.getIngredientManager().addIngredientsAtRuntime(
                ForgeTypes.FLUID_STACK, fluidIngredients
        );
    }
    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(OccultEngineering.MODID, "jei");
    }
    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(AllBlocks.ENCASED_FAN.asStack(), JeiRecipeTypes.SPIRIT_FIRE);
    }
}
