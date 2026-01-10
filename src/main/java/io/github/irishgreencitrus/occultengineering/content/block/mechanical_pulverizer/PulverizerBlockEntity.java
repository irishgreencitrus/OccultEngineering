package io.github.irishgreencitrus.occultengineering.content.block.mechanical_pulverizer;

import com.klikli_dev.occultism.crafting.recipe.CrushingRecipe;
import com.klikli_dev.occultism.crafting.recipe.TieredSingleRecipeInput;
import com.klikli_dev.occultism.registry.OccultismRecipes;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.sound.SoundScapes;
import io.github.irishgreencitrus.occultengineering.content.block.OcEngBlockStates;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringBlockEntities;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class PulverizerBlockEntity extends KineticBlockEntity {
    public ItemStackHandler inputInv;
    public ItemStackHandler outputInv;

    public IItemHandler capability;
    public int timer;
    private int tier;

    // This is Occultism's CrushingRecipe, not Create's
    private RecipeHolder<CrushingRecipe> lastRecipe;

    public PulverizerBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        inputInv = new ItemStackHandler(1);
        outputInv = new ItemStackHandler(1);
        capability = new PulverizerInventoryHandler();
        tier = 1;
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                OccultEngineeringBlockEntities.MECHANICAL_PULVERIZER.get(),
                (be, context) -> be.capability
        );
    }

    public int getTier() {
        return tier;
    }

    public void setTier(int tier) {
        this.tier = tier;
        if (level != null && !level.isClientSide) {
            var state = getBlockState();
            if (state.hasProperty(OcEngBlockStates.TIER)) {
                level.setBlock(worldPosition, state.setValue(OcEngBlockStates.TIER, tier), Block.UPDATE_ALL);
            }
        }
        setChanged();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void tickAudio() {
        super.tickAudio();
        if (getSpeed() == 0) return;
        if (inputInv.getStackInSlot(0).isEmpty()) return;
        float pitch = Mth.clamp((Math.abs(getSpeed()) / 256f) + .45f, .85f, 1f);
        SoundScapes.play(SoundScapes.AmbienceGroup.CRUSHING, worldPosition, pitch);
    }

    @Override
    public void tick() {
        super.tick();
        if (getSpeed() == 0) return;

        if (outputInv.getStackInSlot(0).getCount() == outputInv.getStackInSlot(0).getMaxStackSize()) return;

        if (level == null) return;

        if (timer <= 0) {
            if (inputInv.getStackInSlot(0).isEmpty()) return;
            var recipe = findCurrentRecipe(inputInv.getStackInSlot(0));

            timer = recipe
                    .map(crushingRecipeRecipeHolder -> crushingRecipeRecipeHolder.value().getCrushingTime())
                    .orElse(100);
            notifyUpdate();
        } else {
            timer -= getProcessingSpeed();
            if (level.isClientSide) {
                spawnParticles();
                return;
            }
            if (timer <= 0) {
                process();
                notifyUpdate();
            }
        }
    }

    @Override
    public void invalidate() {
        super.invalidate();
        invalidateCapabilities();
    }

    @Override
    public void destroy() {
        super.destroy();
        ItemHelper.dropContents(level, worldPosition, inputInv);
        ItemHelper.dropContents(level, worldPosition, outputInv);
    }

    public int getProcessingSpeed() {
        return Mth.clamp((int) Math.abs((tier * getSpeed()) / 32f), 1, 512);
    }

    private void process() {
        if (level == null) return;
        var inputStack = inputInv.getStackInSlot(0);

        var recipe = findCurrentRecipe(inputStack);

        if (recipe.isEmpty()) return;

        var input = new TieredSingleRecipeInput(inputStack, this.tier);
        var result = recipe.get().value().assemble(input, level.registryAccess());

        var remainder = ItemHandlerHelper.insertItem(outputInv, result, true);
        // If we can't fit the remainder, don't process it
        if (!remainder.isEmpty()) return;

        inputStack.shrink(1);
        inputInv.setStackInSlot(0, inputStack);
        ItemHandlerHelper.insertItem(outputInv, result, false);
    }

    private void spawnParticles() {
        if (level == null) return;
        if (level.random.nextInt(3) != 0) return;
        ItemStack item = inputInv.getStackInSlot(0);
        if (item.isEmpty()) return;

        ItemParticleOption data = new ItemParticleOption(ParticleTypes.ITEM, item);
        var yRot = -getBlockState().getValue(PulverizerBlock.HORIZONTAL_FACING).toYRot();

        var center = worldPosition.getCenter();
        var offset = VecHelper.rotate(new Vec3(0, 0, 8 / 16f), yRot, Direction.Axis.Y);
        offset = VecHelper.offsetRandomly(offset, level.random, 1 / 64f);
        center = center.add(offset);

        var target = VecHelper.rotate(new Vec3(0, -0.5f, 0.2f), yRot, Direction.Axis.Y);
        level.addParticle(data, center.x, center.y, center.z, target.x, target.y, target.z);
    }

    public boolean canProcess(ItemStack stack) {
        if (level == null) return false;

        // Can't process a different item if there's already one in the output slot
        var recipe = findCurrentRecipe(stack);
        if (recipe.isEmpty()) return false;

        var emptySlot = outputInv.getStackInSlot(0).isEmpty();
        if (emptySlot) return true;


        var input = new TieredSingleRecipeInput(stack, this.tier);

        var matchingItem = outputInv.getStackInSlot(0).is(
                recipe.get().value().assemble(input, level.registryAccess()).getItem());

        if (!matchingItem) return false;

        var output = recipe.get().value().assemble(input, level.registryAccess());
        var remainder = ItemHandlerHelper.insertItem(outputInv, output, true);

        // We won't process something if it puts it over our stack size
        return remainder.isEmpty();
    }

    private Optional<RecipeHolder<CrushingRecipe>> findCurrentRecipe(ItemStack stack) {
        if (level == null) return Optional.empty();
        var input = new TieredSingleRecipeInput(stack, this.tier);

        if (lastRecipe != null && lastRecipe.value().matches(input, level)) {
            return Optional.ofNullable(lastRecipe);
        }

        var recipe = level.getRecipeManager().getRecipeFor(OccultismRecipes.CRUSHING_TYPE.get(), input, level);

        recipe.ifPresent(
                r -> lastRecipe = r);

        return recipe;
    }

    @Override
    protected void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        compound.putInt("timer", timer);
        compound.putInt("tier", tier);
        compound.put("input_inventory", inputInv.serializeNBT(registries));
        compound.put("output_inventory", outputInv.serializeNBT(registries));
        super.write(compound, registries, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        timer = compound.getInt("timer");
        tier = compound.getInt("tier");
        inputInv.deserializeNBT(registries, compound.getCompound("input_inventory"));
        outputInv.deserializeNBT(registries, compound.getCompound("output_inventory"));
        super.read(compound, registries, clientPacket);
    }

    private class PulverizerInventoryHandler extends CombinedInvWrapper {
        public PulverizerInventoryHandler() {
            super(inputInv, outputInv);
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            if (outputInv == getHandlerFromIndex(getIndexForSlot(slot)))
                return false;
            return canProcess(stack) && super.isItemValid(slot, stack);
        }

        @Override
        public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            if (outputInv == getHandlerFromIndex(getIndexForSlot(slot)))
                return stack;
            if (!isItemValid(slot, stack))
                return stack;
            return super.insertItem(slot, stack, simulate);
        }

        @Override
        public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (inputInv == getHandlerFromIndex(getIndexForSlot(slot)))
                return ItemStack.EMPTY;
            return super.extractItem(slot, amount, simulate);
        }
    }
}
