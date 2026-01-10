package io.github.irishgreencitrus.occultengineering.compat.jei;

import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import io.github.irishgreencitrus.occultengineering.content.item.MechanicalUpgradeItem;
import io.github.irishgreencitrus.occultengineering.content.recipe.UpgradeTierRecipe;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringBlocks;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringItems;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UpgradeTierRecipeMaker {
    public static List<RecipeHolder<CraftingRecipe>> createRecipes() {
        return List.of(
                makeRecipe(OccultEngineeringBlocks.MECHANICAL_PULVERIZER.asStack(), OccultEngineeringItems.MECHANICAL_UPGRADE_DJINNI.asStack()),
                makeRecipe(OccultEngineeringBlocks.MECHANICAL_PULVERIZER.asStack(), OccultEngineeringItems.MECHANICAL_UPGRADE_AFRIT.asStack()),
                makeRecipe(OccultEngineeringBlocks.MECHANICAL_PULVERIZER.asStack(), OccultEngineeringItems.MECHANICAL_UPGRADE_MARID.asStack())
        );
    }

    public static RecipeHolder<CraftingRecipe> makeRecipe(@NotNull ItemStack item, @NotNull ItemStack upgrade) {
        String group = "occultengineering.mechanical_upgrade";
        var tier = 1;
        if (upgrade.getItem() instanceof MechanicalUpgradeItem mui) {
            tier = mui.getTier();
        }
        var id = ResourceLocation.fromNamespaceAndPath(OccultEngineering.MODID, upgrade.getDescriptionId());
        var recipe = new ShapelessRecipe(group, CraftingBookCategory.MISC,
                UpgradeTierRecipe.withTier(item, tier),
                NonNullList.of(Ingredient.EMPTY, Ingredient.of(item), Ingredient.of(upgrade)));
        return new RecipeHolder<>(id, recipe);
    }

}
