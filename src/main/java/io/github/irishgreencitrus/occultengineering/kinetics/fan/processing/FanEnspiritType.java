package io.github.irishgreencitrus.occultengineering.kinetics.fan.processing;

import com.klikli_dev.occultism.crafting.recipe.ItemStackFakeInventory;
import com.klikli_dev.occultism.crafting.recipe.SpiritFireRecipe;
import com.klikli_dev.occultism.registry.OccultismEffects;
import com.klikli_dev.occultism.registry.OccultismRecipes;
import com.simibubi.create.content.kinetics.fan.processing.FanProcessingType;
import com.simibubi.create.foundation.recipe.RecipeApplier;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class FanEnspiritType implements FanProcessingType {
    @Override
    public boolean isValidAt(Level level, BlockPos blockPos) {
        BlockState blockState = level.getBlockState(blockPos);
        return blockState.is(OccultEngineeringTags.ENSPIRIT_CATALYST);
    }

    @Override
    public int getPriority() {
        return 500;
    }

    @Override
    public boolean canProcess(ItemStack itemStack, Level level) {
        ItemStackFakeInventory fakeInventory = new ItemStackFakeInventory(ItemStack.EMPTY);
        fakeInventory.setItem(0, itemStack);
        Optional<SpiritFireRecipe> recipe = level.getRecipeManager().getRecipeFor(
                OccultismRecipes.SPIRIT_FIRE_TYPE.get(), fakeInventory, level
        );
        return recipe.isPresent();
    }

    @Override
    public @Nullable List<ItemStack> process(ItemStack itemStack, Level level) {
        ItemStackFakeInventory fakeInventory = new ItemStackFakeInventory(ItemStack.EMPTY);
        fakeInventory.setItem(0, itemStack);
        Optional<SpiritFireRecipe> recipe = level.getRecipeManager().getRecipeFor(
                OccultismRecipes.SPIRIT_FIRE_TYPE.get(), fakeInventory, level
        );
        return recipe.map(
                spiritFireRecipe -> RecipeApplier.applyRecipeOn(level, itemStack, spiritFireRecipe)
        ).orElse(null);
    }

    @Override
    public void spawnProcessingParticles(Level level, Vec3 pos) {
        if (level.random.nextInt(8) != 0)
            return;
        level.addParticle(ParticleTypes.PORTAL, pos.x, pos.y + .25f, pos.z, 1 /16f, 1 / 16f, 1/16f);
    }

    @Override
    public void morphAirFlow(AirFlowParticleAccess airFlowParticleAccess, RandomSource randomSource) {
        airFlowParticleAccess.setColor(0x5a09b0);
        airFlowParticleAccess.setAlpha(1.0f);
        if (randomSource.nextInt(6) == 0)
            airFlowParticleAccess.spawnExtraParticle(ParticleTypes.PORTAL, 1.0f);
    }

    @Override
    public void affectEntity(Entity entity, Level level) {
        if (entity instanceof Player p) {
            p.addEffect(new MobEffectInstance(OccultismEffects.THIRD_EYE.get(), 5*20, 1));
            p.addEffect(new MobEffectInstance(MobEffects.HUNGER, 15 * 20, 1));
        }
    }
}
