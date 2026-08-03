package io.github.irishgreencitrus.occultengineering.content.recipe;

import io.github.irishgreencitrus.occultengineering.content.block.mechanical_pulverizer.PulverizerBlockItem;
import io.github.irishgreencitrus.occultengineering.content.item.MechanicalUpgradeItem;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringRecipes;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class UpgradeTierRecipe extends CustomRecipe {
    public UpgradeTierRecipe(ResourceLocation id, CraftingBookCategory category) {
        super(id, category);
    }

    @Override
    public boolean matches(CraftingContainer input, Level level) {
        boolean foundItem = false;
        boolean foundUpgrade = false;
        for (int slot = 0; slot < input.getContainerSize(); slot++) {
            var stack = input.getItem(slot);
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

    public static ItemStack withTier(ItemStack original, int tier) {
        var upgradableItem = original.copy();
        PulverizerBlockItem.setTier(upgradableItem, tier);
        return upgradableItem;
    }

    @Override
    public ItemStack assemble(CraftingContainer input, RegistryAccess registryAccess) {
        var upgradableItem = ItemStack.EMPTY;
        int oldTier = 0;
        int newTier = 0;

        for (int slot = 0; slot < input.getContainerSize(); slot++) {
            var stack = input.getItem(slot);
            if (stack.isEmpty()) continue;
            if (stack.getItem() instanceof MechanicalUpgradeItem upgrade) {
                newTier = upgrade.getTier();
            } else if (stack.getItem() instanceof PulverizerBlockItem) {
                oldTier = PulverizerBlockItem.getTier(stack);
                upgradableItem = stack.copy();
            } else {
                return ItemStack.EMPTY;
            }
        }

        if (newTier <= oldTier) return ItemStack.EMPTY;
        PulverizerBlockItem.setTier(upgradableItem, newTier);
        return upgradableItem;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return OccultEngineeringRecipes.UPGRADE_TIER.get();
    }
}
