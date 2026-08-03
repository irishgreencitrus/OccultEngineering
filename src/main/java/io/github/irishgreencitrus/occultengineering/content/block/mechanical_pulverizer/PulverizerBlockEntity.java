package io.github.irishgreencitrus.occultengineering.content.block.mechanical_pulverizer;

import com.klikli_dev.occultism.Occultism;
import com.klikli_dev.occultism.crafting.recipe.CrushingRecipe;
import com.klikli_dev.occultism.crafting.recipe.TieredItemStackFakeInventory;
import com.klikli_dev.occultism.registry.OccultismRecipes;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.sound.SoundScapes;
import io.github.irishgreencitrus.occultengineering.content.block.OcEngBlockStates;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class PulverizerBlockEntity extends KineticBlockEntity {
    private static final float LEGACY_MARID_TIME_MULTIPLIER = 0.2f;
    private static final float PARITY_MARID_TIME_MULTIPLIER = 0.3f;

    public ItemStackHandler inputInv;
    public ItemStackHandler outputInv;
    public LazyOptional<IItemHandler> capability;
    public int timer;
    private int tier;
    // This is Occultism's CrushingRecipe, not Create's
    private CrushingRecipe lastRecipe;

    public PulverizerBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        inputInv = new ItemStackHandler(1);
        outputInv = new ItemStackHandler(1);
        capability = LazyOptional.of(PulverizerInventoryHandler::new);
        tier = 1;
    }

    public int getTier() {
        return tier;
    }

    public void setTier(int tier) {
        this.tier = tier;
        lastRecipe = null;
        if (level != null && !level.isClientSide) {
            var state = getBlockState();
            if (state.hasProperty(OcEngBlockStates.TIER)) {
                level.setBlock(worldPosition, state.setValue(OcEngBlockStates.TIER, tier), 3);
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
                    .map(CrushingRecipe::getCrushingTime)
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
        capability.invalidate();
    }

    @Override
    public void destroy() {
        super.destroy();
        ItemHelper.dropContents(level, worldPosition, inputInv);
        ItemHelper.dropContents(level, worldPosition, outputInv);
    }

    private void process() {
        if (level == null) return;
        var inputStack = inputInv.getStackInSlot(0);
        var recipe = findCurrentRecipe(inputStack);

        if (recipe.isEmpty()) return;

        var result = assembleResult(recipe.get(), inputStack);

        var remainder = ItemHandlerHelper.insertItem(outputInv, result, true);
        // If we can't fit the remainder, don't process it.
        if (!remainder.isEmpty()) return;

        inputStack.shrink(1);
        inputInv.setStackInSlot(0, inputStack);
        ItemHandlerHelper.insertItem(outputInv, result, false);
    }

    public boolean canProcess(ItemStack stack) {
        if (level == null) return false;

        // Can't process a different item if there's already something in the output slot.
        var recipe = findCurrentRecipe(stack);
        if (recipe.isEmpty()) return false;

        var emptySlot = outputInv.getStackInSlot(0).isEmpty();
        if (emptySlot) return true;


        var output = assembleResult(recipe.get(), stack);
        var matchingItem = outputInv.getStackInSlot(0).is(output.getItem());

        if (!matchingItem) return false;

        var remainder = ItemHandlerHelper.insertItem(outputInv, output, true);

        // We won't process something if it puts it over our stack size
        return remainder.isEmpty();
    }

    private ItemStack assembleResult(CrushingRecipe recipe, ItemStack stack) {
        var input = new TieredItemStackFakeInventory(stack, tier);
        var result = recipe.assemble(input, level.registryAccess());
        if (getOutputMultiplier() > 0 && !recipe.getIgnoreCrushingMultiplier()) {
            result.setCount((int) (result.getCount() * getOutputMultiplier()));
        }
        return result;
    }


    private Optional<CrushingRecipe> findCurrentRecipe(ItemStack stack) {
        if (level == null) return Optional.empty();
        var input = new TieredItemStackFakeInventory(stack, tier);

        if (lastRecipe != null && lastRecipe.matches(input, level)) {
            return Optional.ofNullable(lastRecipe);
        }

        var recipe = level.getRecipeManager().getRecipeFor(OccultismRecipes.CRUSHING_TYPE.get(), input, level);
        recipe.ifPresent(r -> lastRecipe = r);
        return recipe;
    }

    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        compound.putInt("timer", timer);
        compound.putInt("tier", tier);
        compound.put("input_inventory", inputInv.serializeNBT());
        compound.put("output_inventory", outputInv.serializeNBT());
        super.write(compound, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        timer = compound.getInt("timer");
        tier = compound.contains("tier") ? compound.getInt("tier") : 1;
        inputInv.deserializeNBT(compound.getCompound("input_inventory"));
        outputInv.deserializeNBT(compound.getCompound("output_inventory"));
        super.read(compound, clientPacket);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (isItemHandlerCap(cap)) return capability.cast();
        return super.getCapability(cap, side);
    }

    public int getProcessingSpeed() {
        return Mth.clamp((int) Math.abs(getSpeed() / getTimeMultiplier() / 32f), 1, 512);
    }

    public float getOutputMultiplier() {
        return switch (tier) {
            case 2 -> Occultism.SERVER_CONFIG.spiritJobs.tier2CrusherOutputMultiplier.get().floatValue();
            case 3 -> Occultism.SERVER_CONFIG.spiritJobs.tier3CrusherOutputMultiplier.get().floatValue();
            case 4 -> Occultism.SERVER_CONFIG.spiritJobs.tier4CrusherOutputMultiplier.get().floatValue();
            default -> Occultism.SERVER_CONFIG.spiritJobs.tier1CrusherOutputMultiplier.get().floatValue();
        };
    }

    public float getTimeMultiplier() {
        return switch (tier) {
            case 2 -> Occultism.SERVER_CONFIG.spiritJobs.tier2CrusherTimeMultiplier.get().floatValue();
            case 3 -> Occultism.SERVER_CONFIG.spiritJobs.tier3CrusherTimeMultiplier.get().floatValue();
            case 4 -> {
                var configured = Occultism.SERVER_CONFIG.spiritJobs.tier4CrusherTimeMultiplier.get().floatValue();
                // Occultism 1.20's legacy default is 0.2, while the 1.21 default used by this
                // feature is 0.3. Preserve every other server configuration value.
                yield configured == LEGACY_MARID_TIME_MULTIPLIER
                        ? PARITY_MARID_TIME_MULTIPLIER
                        : configured;
            }
            default -> Occultism.SERVER_CONFIG.spiritJobs.tier1CrusherTimeMultiplier.get().floatValue();
        };
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
