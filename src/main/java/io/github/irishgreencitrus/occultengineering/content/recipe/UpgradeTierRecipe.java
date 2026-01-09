package io.github.irishgreencitrus.occultengineering.content.recipe;

import io.github.irishgreencitrus.occultengineering.content.block.mechanical_pulverizer.PulverizerBlockItem;
import io.github.irishgreencitrus.occultengineering.content.item.MechanicalUpgradeItem;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringDataComponents;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class UpgradeTierRecipe extends CustomRecipe {
    public UpgradeTierRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        boolean foundItem = false;
        boolean foundUpgrade = false;
        for (var stack : input.items()) {
            if (stack.isEmpty()) continue;
            if (stack.getItem() instanceof MechanicalUpgradeItem) {
                if (foundUpgrade) return false;
                foundUpgrade = true;
            }
            if (stack.getItem() instanceof PulverizerBlockItem) {
                if (foundItem) return false;
                foundItem = true;
            }
        }
        return foundItem && foundUpgrade;
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        var upgradableItem = ItemStack.EMPTY;

        var oldTier = 0;
        var newTier = 0;

        for (var stack : input.items()) {
            if (stack.isEmpty()) continue;
            if (stack.getItem() instanceof MechanicalUpgradeItem mui) {
                newTier = mui.getTier();
            } else if (stack.getItem() instanceof PulverizerBlockItem pbi) {
                oldTier = stack.getOrDefault(OccultEngineeringDataComponents.CRUSHING_ITEM_TIER, 1);
                upgradableItem = stack.copy();
            } else {
                return ItemStack.EMPTY;
            }
        }

        if (newTier <= oldTier) return ItemStack.EMPTY;
        upgradableItem.set(OccultEngineeringDataComponents.CRUSHING_ITEM_TIER, newTier);
        return upgradableItem;
    }

    @Override
    public boolean canCraftInDimensions(int w, int h) {
        return w * h >= 2;
    }


    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return OccultEngineeringRecipes.UPGRADE_TIER.get();
    }
}
