package io.github.irishgreencitrus.occultengineering.block;

import com.klikli_dev.modonomicon.api.multiblock.Multiblock;
import com.klikli_dev.occultism.common.ritual.CraftMinerSpiritRitual;
import com.klikli_dev.occultism.common.ritual.CraftRitual;
import com.klikli_dev.occultism.common.ritual.CraftWithSpiritNameRitual;
import com.klikli_dev.occultism.common.ritual.Ritual;
import com.klikli_dev.occultism.crafting.recipe.RitualRecipe;
import com.klikli_dev.occultism.registry.OccultismRecipes;
import com.klikli_dev.occultism.util.ItemNBTUtil;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import plus.dragons.createdragonlib.lang.LangBuilder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


public class MechanicalChamberBlockEntity extends KineticBlockEntity {

    public @Nullable RitualRecipe currentRitualRecipe;
    public ResourceLocation currentRitualRecipeId;
    public List<Ingredient> remainingIngredients = new ArrayList<>();
    public List<ItemStack> consumedIngredients = new ArrayList<>();
    public boolean sacrificeProvided;
    public boolean itemUseProvided;
    public int currentTime;

    public ItemStackHandler itemStackHandler;
    public LazyOptional<ItemStackHandler> lazyItemStackHandler = LazyOptional.of(() -> this.itemStackHandler);

    public long lastChangeTime;

    public MechanicalChamberBlockEntity(BlockEntityType<?> entityType, BlockPos worldPos, BlockState state) {
        super(entityType, worldPos, state);

        this.itemStackHandler = new ItemStackHandler(1) {
            @Override
            public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
                var insertResult = super.insertItem(slot, stack, simulate);

                if (MechanicalChamberBlockEntity.this.getCurrentRitualRecipe() != null)
                    return insertResult;

                var self = MechanicalChamberBlockEntity.this;
                var level = self.level;

                assert level != null;

                var ritualRecipe = getRitualFor(level, self.getBlockPos(), stack, null).orElse(null);

                if (ritualRecipe == null)
                    return insertResult;


                if (!simulate && insertResult.getCount() != stack.getCount()) {
                    if (ritualRecipe.getRitual().areAdditionalIngredientsFulfilled(level, self.getBlockPos(), ritualRecipe.getIngredients())) {
                        self.startRitual(ritualRecipe);
                    }
                }
                return insertResult;
            }

            @Override
            public int getSlotLimit(int slot) {
                return 1;
            }

            @Override
            protected void onContentsChanged(int slot) {
                var self = MechanicalChamberBlockEntity.this;

                assert self.level != null;

                if (!self.level.isClientSide) {
                    self.lastChangeTime = MechanicalChamberBlockEntity.this.level.getGameTime();
                    updateBlock();
                }
            }

        };
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction direction) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return this.lazyItemStackHandler.cast();
        }
        return super.getCapability(cap, direction);
    }

    public @Nullable RitualRecipe getCurrentRitualRecipe() {
        // If we don't have a ritual recipe, refresh it from the server.
        if (currentRitualRecipeId != null && level != null) {
            Optional<? extends Recipe<?>> recipe = level.getRecipeManager().byKey(currentRitualRecipeId);

            if (recipe.isPresent() && recipe.get() instanceof RitualRecipe ritualRecipe) {
                if (!ritualRecipe.requiresItemUse() && !ritualRecipe.requiresSacrifice()) {
                    currentRitualRecipe = ritualRecipe;
                }
            }

            currentRitualRecipeId = null;
        }
        return currentRitualRecipe;
    }

    public void startRitual(@NotNull RitualRecipe recipe) {
        if (this.level != null && this.level.isClientSide) return;

        // We don't support summoning entities in an automated system!
        assert recipe.getEntityToSummon() == null;

        // We don't support item use or sacrifices in an automated system!
        assert !recipe.requiresItemUse() && !recipe.requiresSacrifice();

        currentRitualRecipe = recipe;
        currentTime = 0;
        sacrificeProvided = false;
        itemUseProvided = false;
        consumedIngredients.clear();
        remainingIngredients = new ArrayList<>(currentRitualRecipe.getIngredients());

        // We don't call Ritual.start(), as it only plays a sound for our purposes!

        updateBlock();

        level.updateNeighborsAt(getBlockPos(), getBlockState().getBlock());
    }

    public boolean activate(Level level, BlockPos blockPos, ItemStack activationItem) {
        if (level.isClientSide) return true;

        if (getCurrentRitualRecipe() == null) {
            Optional<RitualRecipe> ritualRecipe = getRitualFor(level, blockPos, activationItem, null);
            if (ritualRecipe.isPresent()) {
                var recipe = ritualRecipe.get();
                if (recipe.getRitual().areAdditionalIngredientsFulfilled(level, blockPos, recipe.getIngredients())) {
                    itemStackHandler.insertItem(0, activationItem.split(1), false);
                    startRitual(recipe);
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    public Optional<RitualRecipe> getRitualFor(Level level, BlockPos blockPos, ItemStack activationItem, @Nullable List<Ingredient> additionalIngredients) {
        return level
                .getRecipeManager()
                .getAllRecipesFor(OccultismRecipes.RITUAL_TYPE.get())
                .stream()
                .filter(
                        r -> isRitualValid(r, level, blockPos, activationItem, additionalIngredients == null ? r.getIngredients() : additionalIngredients)
                ).findFirst();
    }

    public boolean isRitualValid(RitualRecipe recipe, Level level, BlockPos centerPosition, ItemStack activationItem, List<Ingredient> additionalIngredients) {
        return recipe.getPentacle() != null && recipe.getActivationItem().test(activationItem) &&
                recipe.getRitual().areAdditionalIngredientsFulfilled(level, centerPosition, additionalIngredients) &&
                getPentacle(recipe, level, centerPosition) == recipe.getPentacle();
    }


    public Multiblock getPentacle(RitualRecipe recipe, Level level, BlockPos blockPos) {
        var pentacle = recipe.getPentacle();
        for (var rotation : Rotation.values()) {
            Collection<Multiblock.SimulateResult> results = pentacle.simulate(level, blockPos, rotation, false, false).getSecond();
            Multiblock pentacleCandidate = null;
            for (var result : results) {
                var stateMatcher = result.getStateMatcher();
                if (!stateMatcher.countsTowardsTotalBlocks()) continue;

                // Look for the center block
                pentacleCandidate = pentacle;

                if (!result.getWorldPosition().equals(blockPos)) {
                    // If our test is not fulfilled, we're either looking at the wrong pentacle, or it isn't valid.
                    // Either way, we need to look at the next pentacle.
                    if (!result.test(level, rotation)) {
                        pentacleCandidate = null;
                        break;
                    }
                }
            }
            if (pentacleCandidate != null)
                return pentacleCandidate;
        }
        return null;
    }

    public void tick() {
        super.tick();
        if (level == null) return;
        if (level.isClientSide) return;
        if (!isSpeedRequirementFulfilled()) {
            return;
        }

        RitualRecipe recipe = getCurrentRitualRecipe();
        if (recipe == null) return;

        if (remainingIngredients == null) {
            restoreRemainingIngredients();
            if (remainingIngredients == null) return;
        }

        if (!isRitualValid(recipe, level, getBlockPos(), itemStackHandler.getStackInSlot(0), remainingIngredients)) {
            stopRitual(false);
            return;
        }

        if (level.getGameTime() % 20 == 0) {
            // We don't care about the direction of the input, just the speed.
            currentTime += getRitualSpeedMultiplier();
        }

        if (level.random.nextInt(16) == 0) {
            ((ServerLevel) this.level)
                    .sendParticles(ParticleTypes.SOUL_FIRE_FLAME, this.getBlockPos().getX() + 0.5 + this.level.random.nextGaussian() / 3,
                            this.getBlockPos().getY() + 0.5, this.getBlockPos().getZ() + 0.5 + this.level.random.nextGaussian() / 3, 5,
                            0.0, 0.0, 0.0,
                            0.0);
        }

        // We don't call Ritual.update, it doesn't seem to do anything?
        if (!recipe.getRitual().consumeAdditionalIngredients(level, getBlockPos(), remainingIngredients, currentTime, consumedIngredients)) {
            this.stopRitual(false);
            return;
        }

        if (this.currentTime >= recipe.getDuration())
            this.stopRitual(true);
    }

    private int getRitualSpeedMultiplier() {
        var calc = (int) (Math.abs(getSpeed()) / 32f);
        return calc <= 0 ? 1 : calc;
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        var parent = super.addToGoggleTooltip(tooltip, isPlayerSneaking);
        if (getCurrentRitualRecipe() != null) {
            var builder = new LangBuilder(OccultEngineering.MODID);
            builder
                    .translate("tooltip.ritualspeed")
                    .text(" ")
                    .text(String.valueOf(getRitualSpeedMultiplier()))
                    .text("x")
                    .style(ChatFormatting.GRAY).forGoggles(tooltip);
        }
        return parent;
    }

    private void stopRitual(boolean ritualCompleted) {
        if (level == null) return;
        if (level.isClientSide) return;

        var recipe = getCurrentRitualRecipe();
        if (recipe != null) {
            if (ritualCompleted) {
                ItemStack activationItem = itemStackHandler.getStackInSlot(0);
                // OVERRIDE: Instead of dropping the item on the floor, we leave it in the chamber.
                if (recipe.getRitual() instanceof CraftRitual) {
                    activationItem.shrink(1); // remove activation item.
                    ((ServerLevel) level).sendParticles(ParticleTypes.LARGE_SMOKE, getBlockPos().getX() + 0.5,
                            getBlockPos().getY() + 0.5, getBlockPos().getZ() + 0.5, 1, 0, 0, 0, 0);

                    ItemStack result = recipe.getResultItem(level.registryAccess()).copy();
                    itemStackHandler.insertItem(0, result, false);
                } else if (recipe.getRitual() instanceof CraftWithSpiritNameRitual) {
                    ItemStack copy = activationItem.copy();
                    activationItem.shrink(1); //remove activation item.

                    ((ServerLevel) level).sendParticles(ParticleTypes.LARGE_SMOKE, getBlockPos().getX() + 0.5,
                            getBlockPos().getY() + 0.5, getBlockPos().getZ() + 0.5, 1, 0, 0, 0, 0);

                    ItemStack result = recipe.getResultItem(level.registryAccess()).copy();
                    ItemNBTUtil.setBoundSpiritName(result, ItemNBTUtil.getBoundSpiritName(copy));
                    itemStackHandler.insertItem(0, result, false);
                } else if (recipe.getRitual() instanceof CraftMinerSpiritRitual) {
                    ItemStack copy = activationItem.copy();
                    activationItem.shrink(1); //remove activation item.

                    ((ServerLevel) level).sendParticles(ParticleTypes.LARGE_SMOKE, getBlockPos().getX() + 0.5,
                            getBlockPos().getY() + 0.5, getBlockPos().getZ() + 0.5, 1, 0, 0, 0, 0);

                    ItemStack result = recipe.getResultItem(level.registryAccess()).copy();

                    //sets up nbt configuration for miner
                    result.getItem().onCraftedBy(result, level, null);

                    //copy over spirit name
                    ItemNBTUtil.setBoundSpiritName(result, ItemNBTUtil.getBoundSpiritName(copy));
                    itemStackHandler.insertItem(0, result, false);
                } else {
                    recipe.getRitual().finish(this.level, this.getBlockPos(), null, null, activationItem);
                }
            } else {
                //recipe.getRitual().interrupt(this.level, this.getBlockPos(), null, null, itemStackHandler.getStackInSlot(0));
                //Pop activation item back into level
                Containers.dropItemStack(this.level, this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ(),
                        itemStackHandler.extractItem(0, 1, false));
            }
        }
        currentRitualRecipe = null;
        currentRitualRecipeId = null;
        currentTime = 0;
        if (remainingIngredients != null) remainingIngredients.clear();
        consumedIngredients.clear();

        updateBlock();

        level.updateNeighborsAt(getBlockPos(), getBlockState().getBlock());
    }

    private void restoreRemainingIngredients() {
        assert level != null;
        RitualRecipe currentRecipe = getCurrentRitualRecipe();
        if (currentRecipe == null) return;

        if (!this.consumedIngredients.isEmpty()) {
            remainingIngredients = Ritual.getRemainingAdditionalIngredients(currentRecipe.getIngredients(), consumedIngredients);
        } else {
            remainingIngredients = new ArrayList<>(currentRecipe.getIngredients());
        }
    }


    @Override
    public void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);

        if (compound.contains("currentRitual")) {
            this.currentRitualRecipeId = ResourceLocation.parse(compound.getString("currentRitual"));
        }

        this.consumedIngredients.clear();
        if (this.currentRitualRecipeId != null || this.getCurrentRitualRecipe() != null) {
            if (compound.contains("consumedIngredients")) {
                ListTag list = compound.getList("consumedIngredients", Tag.TAG_COMPOUND);
                for (int i = 0; i < list.size(); i++) {
                    ItemStack stack = ItemStack.of(list.getCompound(i));
                    this.consumedIngredients.add(stack);
                }
            }
            this.restoreRemainingIngredients();
        }

        this.lazyItemStackHandler.ifPresent((handler) -> handler.deserializeNBT(compound.getCompound("inventory")));
        this.lastChangeTime = compound.getLong("lastChangeTime");

        this.currentTime = compound.getInt("currentTime");
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        RitualRecipe recipe = this.getCurrentRitualRecipe();
        if (recipe != null) {
            compound.putString("currentRitual", recipe.getId().toString());
            if (!this.consumedIngredients.isEmpty()) {
                ListTag list = new ListTag();
                for (ItemStack stack : this.consumedIngredients) {
                    list.add(stack.serializeNBT());
                }
                compound.put("consumedIngredients", list);
            }
        }
        compound.putLong("lastChangeTime", this.lastChangeTime);
        this.lazyItemStackHandler.ifPresent(handler -> compound.put("inventory", handler.serializeNBT()));
        compound.putInt("currentTime", this.currentTime);

        super.write(compound, clientPacket);
    }

    private void updateBlock() {
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 2);
        }
    }
}