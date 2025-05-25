package io.github.irishgreencitrus.occultengineering.content.block.mechanical_pulverizer;

import com.klikli_dev.occultism.crafting.recipe.CrushingRecipe;
import com.klikli_dev.occultism.crafting.recipe.ItemStackFakeInventory;
import com.klikli_dev.occultism.registry.OccultismRecipes;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.sound.SoundScapes;
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
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class PulverizerBlockEntity extends KineticBlockEntity {
    public ItemStackHandler inputInv;
    public ItemStackHandler outputInv;
    public LazyOptional<IItemHandler> capability;
    public int timer;
    // This is Occultism's CrushingRecipe, not Create's
    private CrushingRecipe lastRecipe;

    public PulverizerBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        inputInv = new ItemStackHandler(1);
        outputInv = new ItemStackHandler(1);
        capability = LazyOptional.of(PulverizerInventoryHandler::new);
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
        for (int i = 0; i < outputInv.getSlots(); i++) {
            if (outputInv.getStackInSlot(i).getCount() == outputInv.getSlotLimit(i)) return;
        }

        if (level == null) return;

        if (timer > 0) {
            timer -= getProcessingSpeed();
            if (level.isClientSide) {
                spawnParticles();
                return;
            }
            if (timer <= 0)
                process();
            return;
        }

        if (inputInv.getStackInSlot(0).isEmpty()) return;
        var inputStack = inputInv.getStackInSlot(0);
        var inventoryIn = new ItemStackFakeInventory(inputStack);
        if (lastRecipe == null || !lastRecipe.matches(inventoryIn, level)) {
            Optional<CrushingRecipe> recipe = level.getRecipeManager().getRecipeFor(OccultismRecipes.CRUSHING_TYPE.get(), inventoryIn, level);
            if (recipe.isPresent()) {
                lastRecipe = recipe.get();
                timer = lastRecipe.getCrushingTime();
            } else {
                timer = 100;
            }
            sendData();
            return;
        }
        timer = lastRecipe.getCrushingTime();
        sendData();
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

    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        compound.putInt("timer", timer);
        compound.put("input_inventory", inputInv.serializeNBT());
        compound.put("output_inventory", outputInv.serializeNBT());
        super.write(compound, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        timer = compound.getInt("timer");
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
        return Mth.clamp((int) Math.abs(getSpeed() / 16f), 1, 512);
    }

    private void process() {
        if (level == null) return;
        var inputStack = inputInv.getStackInSlot(0);
        var inventoryIn = new ItemStackFakeInventory(inputStack);
        if (lastRecipe == null || !lastRecipe.matches(inventoryIn, level)) {
            Optional<CrushingRecipe> recipe = level.getRecipeManager().getRecipeFor(OccultismRecipes.CRUSHING_TYPE.get(), inventoryIn, level);
            if (recipe.isEmpty())
                return;
            lastRecipe = recipe.get();
        }
        inputStack.shrink(1);
        inputInv.setStackInSlot(0, inputStack);
        var result = lastRecipe.getResultItem(level.registryAccess());
        outputInv.setStackInSlot(0, result);

        sendData();
        setChanged();
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
        ItemStackFakeInventory inventory = new ItemStackFakeInventory(stack);

        if (lastRecipe != null && lastRecipe.matches(inventory, level)) {
            return true;
        }
        return level.getRecipeManager().getRecipeFor(OccultismRecipes.CRUSHING_TYPE.get(), inventory, level).isPresent();
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
