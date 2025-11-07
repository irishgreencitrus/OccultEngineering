package io.github.irishgreencitrus.occultengineering.compat.enchant_industry;

import com.klikli_dev.occultism.registry.OccultismItems;
import com.klikli_dev.occultism.registry.OccultismTags;
import com.mojang.serialization.DataResult;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringRecipeTypes;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringTags;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import plus.dragons.createdragonsplus.common.fluids.dye.DyeFluidType;
import plus.dragons.createenchantmentindustry.common.fluids.printer.PrinterBlockEntity;
import plus.dragons.createenchantmentindustry.common.fluids.printer.behaviour.PrintingBehaviour;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BindingBookPrintingBehaviour implements PrintingBehaviour {

    public static Optional<DataResult<PrintingBehaviour>> create(Level level, SmartFluidTankBehaviour tank, ItemStack stack) {
        return stack.is(OccultEngineeringTags.BINDING_BOOK_PRINTABLE) ?
                Optional.of(DataResult.success(new BindingBookPrintingBehaviour()))
                : Optional.empty();
    }

    @Override
    public int getRequiredItemCount(Level level, ItemStack stack) {
        if (stack.is(OccultismItems.BOOK_OF_BINDING_EMPTY))
            return 1;
        return 0;
    }

    @Override
    public int getRequiredFluidAmount(Level level, ItemStack stack, FluidStack fluidStack) {
        return 250;
    }

    @Override
    public ItemStack getResult(Level level, ItemStack stack, FluidStack fluidStack) {
        var recipeType = OccultEngineeringRecipeTypes.DYEING_ITEM_TYPE;
        if (recipeType == null) return stack;

        var fType = fluidStack.getFluidType();
        if (fType instanceof DyeFluidType) {
            RecipeManager rm = level.getRecipeManager();
            var allRecipes = rm.getAllRecipesFor(recipeType.get());
            for (RecipeHolder<DyeingItemRecipe> holder : allRecipes) {
                if (holder.value().matches(stack, fluidStack)) {
                    return holder.value().getOutput();
                }
            }
        }
        return stack;
    }

    @Override
    public void onFinished(Level level, BlockPos pos, PrinterBlockEntity printer) {
        level.playLocalSound(pos.below(), SoundEvents.BOOK_PAGE_TURN, SoundSource.BLOCKS, 1.0F, 1.0F, false);
    }
}
