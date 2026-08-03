package io.github.irishgreencitrus.occultengineering.compat.jei;

import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import io.github.irishgreencitrus.occultengineering.content.item.MechanicalUpgradeItem;
import io.github.irishgreencitrus.occultengineering.content.recipe.UpgradeTierRecipe;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringBlocks;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringItems;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class UpgradeTierRecipeMaker {
    private static final String GROUP = "occultengineering.mechanical_upgrade";

    private UpgradeTierRecipeMaker() {
    }

    public static List<CraftingRecipe> createRecipes() {
        return List.of(
                makeRecipe(OccultEngineeringBlocks.MECHANICAL_PULVERIZER.asStack(),
                        OccultEngineeringItems.MECHANICAL_UPGRADE_DJINNI.asStack()),
                makeRecipe(OccultEngineeringBlocks.MECHANICAL_PULVERIZER.asStack(),
                        OccultEngineeringItems.MECHANICAL_UPGRADE_AFRIT.asStack()),
                makeRecipe(OccultEngineeringBlocks.MECHANICAL_PULVERIZER.asStack(),
                        OccultEngineeringItems.MECHANICAL_UPGRADE_MARID.asStack())
        );
    }

    private static CraftingRecipe makeRecipe(@NotNull ItemStack item, @NotNull ItemStack upgrade) {
        int tier = upgrade.getItem() instanceof MechanicalUpgradeItem mechanicalUpgrade
                ? mechanicalUpgrade.getTier()
                : 1;
        ResourceLocation id = OccultEngineering.asResource(upgrade.getDescriptionId());
        return new ShapelessRecipe(
                id,
                GROUP,
                CraftingBookCategory.MISC,
                UpgradeTierRecipe.withTier(item, tier),
                NonNullList.of(Ingredient.EMPTY, Ingredient.of(item), Ingredient.of(upgrade))
        );
    }
}
