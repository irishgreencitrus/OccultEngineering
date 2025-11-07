package io.github.irishgreencitrus.occultengineering.compat.enchant_industry;

import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringRecipeTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import plus.dragons.createdragonsplus.common.fluids.dye.DyeFluidType;

public class DyeingItemRecipe extends CustomRecipe {
    private final Ingredient input;
    private final DyeColor requiredColor;
    private final int requiredAmount;
    private final ItemStack output;

    public Ingredient getInput() {
        return input;
    }

    public DyeColor getRequiredColor() {
        return requiredColor;
    }

    public int getRequiredAmount() {
        return requiredAmount;
    }

    public ItemStack getOutput() {
        return output;
    }

    public DyeingItemRecipe(Ingredient input, DyeColor requiredColor, int requiredAmount, ItemStack output) {
        super(CraftingBookCategory.MISC);
        this.input = input;
        this.requiredColor = requiredColor;
        this.requiredAmount = requiredAmount;
        this.output = output;
    }

    public boolean matches(ItemStack stack, FluidStack fluid) {
        if (!input.test(stack)) return false;
        var ft = fluid.getFluidType();
        if (ft instanceof DyeFluidType dt) {
            return dt.getColor() == requiredColor;
        }
        return false;
    }

    @Override
    public boolean matches(CraftingInput craftingInput, Level level) {
        return false;
    }

    @Override
    public ItemStack assemble(CraftingInput craftingInput, HolderLookup.Provider provider) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    public static DyeingItemRecipeSerializer SERIALIZER = new DyeingItemRecipeSerializer();
    @Override
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return OccultEngineeringRecipeTypes.DYEING_ITEM_TYPE.get();
    }
}
