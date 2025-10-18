package io.github.irishgreencitrus.occultengineering.content.block.mechanical_chamber;

import com.klikli_dev.occultism.crafting.recipe.RitualRecipe;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;


public interface IRitualProcessor {
    Optional<RecipeHolder<RitualRecipe>> getRitualRecipe();
    boolean isRitualActive();
    int getTier();
    void tick();
    void startRitual(@Nullable ServerPlayer player, ItemStack activationItem, RecipeHolder<RitualRecipe> ritualRecipe);
    void stopRitual(boolean finished);
}
