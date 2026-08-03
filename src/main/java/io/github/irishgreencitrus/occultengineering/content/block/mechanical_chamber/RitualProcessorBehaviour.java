package io.github.irishgreencitrus.occultengineering.content.block.mechanical_chamber;

import com.klikli_dev.occultism.common.ritual.*;
import com.klikli_dev.occultism.crafting.recipe.RitualRecipe;
import com.klikli_dev.occultism.registry.OccultismDataComponents;
import com.klikli_dev.occultism.registry.OccultismParticles;
import com.klikli_dev.occultism.util.ItemNBTUtil;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import io.github.irishgreencitrus.occultengineering.mixin.accessor.SummonRitualAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

// This replaces the functionality of the GoldenSacrificialBowlBlockEntity
// but removes references to players activating it and adds a few new features.
// It's enough different that I couldn't just make a Pull Request.
public class RitualProcessorBehaviour extends BlockEntityBehaviour implements IRitualProcessor {
    public static BehaviourType<RitualProcessorBehaviour> TYPE = new BehaviourType<>();

    public @Nullable RecipeHolder<RitualRecipe> currentRitualRecipe;
    public @Nullable ResourceLocation currentRitualRecipeId;
    public @Nullable List<Ingredient> remainingAdditionalIngredients = new ArrayList<>();
    public @NotNull List<ItemStack> consumedIngredients = new ArrayList<>();
    public int tier = 1;
    public int currentTime = 0;
    public Supplier<ItemStackHandler> itemStackHandlerGetter;
    public Supplier<Integer> ritualSpeedMultiplier;

    public RitualProcessorBehaviour(SmartBlockEntity be, Supplier<ItemStackHandler> itemStackHandlerGetter, Supplier<Integer> ritualSpeedMultiplier) {
        super(be);
        this.itemStackHandlerGetter = itemStackHandlerGetter;
        this.ritualSpeedMultiplier = ritualSpeedMultiplier;
    }

    @Override
    public void tick() {
        super.tick();
        var maybeRecipe = getRitualRecipe();
        var level = blockEntity.getLevel();
        if (level == null || level.isClientSide || maybeRecipe.isEmpty()) return;
        var serverLevel = (ServerLevel) level;
        var recipe = maybeRecipe.get();

        if (remainingAdditionalIngredients == null) {
            restoreRemainingAdditionalIngredients();
            if (remainingAdditionalIngredients == null) return;
        }

        var handler = itemStackHandlerGetter.get();
        if (!ritualIsValid(recipe.value(), level, blockEntity.getBlockPos(), handler.getStackInSlot(0), remainingAdditionalIngredients)) {
            stopRitual(false);
            return;
        }

        if (level.random.nextInt(16) == 0) {
            serverLevel.sendParticles(ParticleTypes.PORTAL,
                    getPos().getX() + 0.5 + level.random.nextGaussian() / 3.0,
                    getPos().getY() + 0.5 + level.random.nextGaussian() / 3.0,
                    getPos().getZ() + 0.5 + level.random.nextGaussian() / 3.0,
                    5,
                    0,
                    0,
                    0,
                    0);
        }

        if (level.getGameTime() % 5 == 0) {
            if (!remainingAdditionalIngredients.isEmpty())
                recipe.value().getRitual().markNextIngredient(level, getPos(), remainingAdditionalIngredients.getFirst(), getTier());
            else {
                var time = level.getGameTime() / 20.0;
                var sinAdjusted = Math.sin(time) * 0.3;
                var cosAdjusted = Math.cos(time) * 0.3;
                var centre = getPos().getCenter();
                serverLevel.sendParticles(OccultismParticles.SPIRIT_FIRE_FLAME.get(),
                        centre.x + cosAdjusted,
                        centre.y + 0.2 + cosAdjusted,
                        centre.z + sinAdjusted,
                        1, 0, 0, 0, 0.003);
            }
        }

        // 1 Hz
        if (level.getGameTime() % 20 == 0) {
            // Double-check we're never decreasing the currentTime.
            currentTime += Math.abs(ritualSpeedMultiplier.get());
        }

        // We can't call getRitual().update(), as it requires the block entity to be
        // an instance of GoldenSacrificialBowlBlockEntity.
        // Besides, it doesn't do anything in Occultism's default ritual types.

        if (!recipe.value().getRitual().consumeAdditionalIngredients(level, getPos(), remainingAdditionalIngredients, currentTime, consumedIngredients)) {
            // If we didn't do this, our ingredients could not be found, so stop the ritual.
            stopRitual(false);
            return;
        }

        if (recipe.value().getDuration() >= 0 && currentTime >= recipe.value().getDuration()) {
            stopRitual(true);
        }
    }

    protected void restoreRemainingAdditionalIngredients() {
        var ritualRecipe = getRitualRecipe();
        if (ritualRecipe.isEmpty() || blockEntity.getLevel() == null) {
            remainingAdditionalIngredients = null;
            return;
        }
        var recipe = ritualRecipe.get().value();

        if (!consumedIngredients.isEmpty()) {
            remainingAdditionalIngredients = Ritual.getRemainingAdditionalIngredients(
                    recipe.getIngredients(), consumedIngredients
            );
        } else {
            remainingAdditionalIngredients = new ArrayList<>(recipe.getIngredients());
        }
    }

    public boolean ritualIsValid(RitualRecipe recipe, Level level, BlockPos goldenBowlPosition, ItemStack activationItem, List<Ingredient> remainingAdditionalIngredients) {
        return recipe.getPentacle() != null && recipe.getActivationItem().test(activationItem) &&
                recipe.getRitual().areAdditionalIngredientsFulfilled(level, goldenBowlPosition, remainingAdditionalIngredients) &&
                recipe.getPentacle().validate(level, goldenBowlPosition) != null;
    }


    @Override
    public void startRitual(@Nullable ServerPlayer player, ItemStack activationItem, RecipeHolder<RitualRecipe> ritualRecipe) {
        var level = blockEntity.getLevel();
        if (level == null) return;
        if (level.isClientSide()) return;

        // These three things are not supported. Yet...
        if (ritualRecipe.value().requiresItemUse()
                || ritualRecipe.value().requiresSacrifice()) {
            OccultEngineering.LOGGER.warn("Ritual {} requires an unsupported feature", ritualRecipe.id());
            return;
        }

        currentRitualRecipe = ritualRecipe;
        currentTime = 0;
        consumedIngredients.clear();
        remainingAdditionalIngredients = new ArrayList<>(ritualRecipe.value().getIngredients());

        blockDirty();
    }

    @Override
    public void stopRitual(boolean ritualCompleted) {
        var level = blockEntity.getLevel();
        if (level == null) return;
        if (level.isClientSide()) return;

        var maybeRecipe = getRitualRecipe();
        if (maybeRecipe.isPresent()) {
            var recipe = maybeRecipe.get();
            var itemStackHandler = itemStackHandlerGetter.get();
            if (ritualCompleted) {
                var activationItem = itemStackHandler.getStackInSlot(0);
                // OVERRIDE: Instead of dropping the item on the floor, we leave it in the chamber.
                switch (recipe.value().getRitual()) {
                    case CraftRitual ignored -> {
                        ((ServerLevel) level).sendParticles(ParticleTypes.LARGE_SMOKE, getPos().getX() + 0.5,
                                getPos().getY() + 0.5, getPos().getZ() + 0.5, 1, 0, 0, 0, 0);

                        ItemStack result = recipe.value().getResultItem(level.registryAccess()).copy();
                        itemStackHandler.setStackInSlot(0, result);

                    }
                    case CraftWithSpiritNameRitual ignored -> {
                        var copy = activationItem.copy();

                        ((ServerLevel) level).sendParticles(ParticleTypes.LARGE_SMOKE, getPos().getX() + 0.5,
                                getPos().getY() + 0.5, getPos().getZ() + 0.5, 1, 0, 0, 0, 0);

                        ItemStack result = recipe.value().getResultItem(level.registryAccess()).copy();
                        ItemNBTUtil.setBoundSpiritName(result, ItemNBTUtil.getBoundSpiritName(copy));
                        itemStackHandler.setStackInSlot(0, result);
                    }
                    case CraftMinerSpiritRitual ignored -> {
                        ItemStack copy = activationItem.copy();
                        activationItem.shrink(1); //remove activation item.

                        ((ServerLevel) level).sendParticles(ParticleTypes.LARGE_SMOKE, getPos().getX() + 0.5,
                                getPos().getY() + 0.5, getPos().getZ() + 0.5, 1, 0, 0, 0, 0);

                        ItemStack result = recipe.value().getResultItem(level.registryAccess()).copy();

                        //sets up nbt configuration for miner
                        result.getItem().onCraftedBy(result, level, null);

                        //copy over spirit name
                        ItemNBTUtil.setBoundSpiritName(result, ItemNBTUtil.getBoundSpiritName(copy));
                        itemStackHandler.setStackInSlot(0, result);
                    }
                    case UpgradeRitual ignored -> {
                        ItemStack copy = activationItem.copy();
                        activationItem.shrink(1); //remove activation item.

                        ItemStack base = consumedIngredients.getFirst().copy();

                        ((ServerLevel) level).sendParticles(ParticleTypes.LARGE_SMOKE, blockEntity.getBlockPos().getX() + 0.5,
                                blockEntity.getBlockPos().getY() + 0.5, blockEntity.getBlockPos().getZ() + 0.5, 1, 0, 0, 0, 0);

                        ItemStack result = recipe.value().getResultItem(level.registryAccess()).copy();
                        Rarity rarity = result.getRarity();

                        result.applyComponents(base.getComponents());
                        if (result.has(DataComponents.MAX_DAMAGE)) {
                            int maxDurability = result.getMaxDamage();
                            result.applyComponents(DataComponentMap.builder()
                                    .set(DataComponents.MAX_DAMAGE, maxDurability)
                                    .build());
                        }
                        if (copy.has(OccultismDataComponents.SPIRIT_NAME))
                            ItemNBTUtil.setBoundSpiritName(result, ItemNBTUtil.getBoundSpiritName(copy));

                        result.applyComponents(DataComponentMap.builder()
                                .set(DataComponents.RARITY, rarity)
                                .build());

                        itemStackHandler.setStackInSlot(0, result);
                    }
                    case SummonSpiritWithJobRitual ritual -> {
                        // this is a little weird, we need to summon the entity
                        // and then immediately bind it to the book
                        ItemStack copy = activationItem.copy();

                        ItemStack result = ritual.getBookOfCallingBound(level.registryAccess(), activationItem);
                        activationItem.shrink(1);


                        ((ServerLevel) level).sendParticles(ParticleTypes.LARGE_SMOKE, blockEntity.getBlockPos().getX() + 0.5,
                                blockEntity.getBlockPos().getY() + 0.5, blockEntity.getBlockPos().getZ() + 0.5, 1, 0, 0, 0, 0);

                        var entityType = ritual.recipe.getEntityToSummon();
                        if (entityType != null) {
                            var entity = entityType.create(level);

                            if (entity instanceof LivingEntity living) {
                                ritual.prepareLivingEntityForSpawn(living,
                                        level, getPos(), null, null,
                                        ItemNBTUtil.getBoundSpiritName(copy), ((SummonRitualAccessor) ritual).getTame());
                                ritual.applyEntityNbt(entity);
                            }

                            var entityData = new CompoundTag();
                            var id = entity.getEncodeId();
                            if (id != null) {
                                entityData.putString("id", id);
                            }
                            entityData = entity.saveWithoutId(entityData);

                            ItemNBTUtil.setSpiritEntityData(result, entityData);
                            ItemNBTUtil.setSpiritEntityUUID(result, entity.getUUID());
                            ItemNBTUtil.setBoundSpiritName(result, entity.getName().getString());
                        }

                        itemStackHandler.setStackInSlot(0, result);
                    }
                    default -> {
                        OccultEngineering.LOGGER.warn("Falling back to default handler for {} with ritual {}", recipe.id(), recipe.value().getRitual().factoryId);
                        try {
                            recipe.value().getRitual().finish(level, getPos(), null, null, activationItem);
                        } catch (NullPointerException exception) {
                            OccultEngineering.LOGGER.error("A null pointer exception occurred with the default handler, dropping activation item and ingredients.");
                            dropStackNearby(level, itemStackHandler.extractItem(0, 1, false));
                            for (var item : consumedIngredients)
                                dropStackNearby(level, item);
                            consumedIngredients.clear();
                        }
                    }
                }
            } else {
                dropStackNearby(level, itemStackHandler.extractItem(0, 1, false));
            }
        }
        currentRitualRecipe = null;
        currentRitualRecipeId = null;
        currentTime = 0;
        if (remainingAdditionalIngredients != null) remainingAdditionalIngredients.clear();
        consumedIngredients.clear();

        blockDirty();
    }

    private void dropStackNearby(Level level, ItemStack stack) {
        Containers.dropItemStack(level, getPos().getX(), getPos().getY(), getPos().getZ(), stack);
    }

    @Override
    public Optional<RecipeHolder<RitualRecipe>> getRitualRecipe() {
        if (this.currentRitualRecipeId != null && blockEntity.getLevel() != null) {
            var level = Objects.requireNonNull(blockEntity.getLevel());
            var recipe = level.getRecipeManager().byKey(currentRitualRecipeId);

            // I don't want a load of warnings, so even though we don't really need a
            // variable, I'm using one anyway
            @SuppressWarnings("unchecked")
            var result = recipe.filter(r -> r.value() instanceof RitualRecipe)
                    .map(r -> (RecipeHolder<RitualRecipe>) r);

            result.ifPresent(
                    ritualRecipeRecipeHolder ->
                            currentRitualRecipe = ritualRecipeRecipeHolder);

            currentRitualRecipeId = null;
        }
        return Optional.ofNullable(currentRitualRecipe);
    }

    @Override
    public void read(CompoundTag nbt, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(nbt, registries, clientPacket);
        this.consumedIngredients.clear();
        itemStackHandlerGetter.get().deserializeNBT(registries, nbt.getCompound("inventory"));
        if (this.currentRitualRecipeId != null || this.getRitualRecipe().isPresent()) {
            if (nbt.contains("consumedIngredients")) {
                ListTag list = nbt.getList("consumedIngredients", Tag.TAG_COMPOUND);
                for (int i = 0; i < list.size(); i++) {
                    ItemStack stack = ItemStack.parseOptional(registries, list.getCompound(i));
                    this.consumedIngredients.add(stack);
                }
            }
            this.restoreRemainingAdditionalIngredients();
        }

        if (nbt.contains("currentRitual")) {
            currentRitualRecipeId = ResourceLocation.parse(nbt.getString("currentRitual"));
        }

        currentTime = nbt.getInt("currentTime");
    }

    @Override
    public void write(CompoundTag nbt, HolderLookup.Provider registries, boolean clientPacket) {
        nbt.put("inventory", itemStackHandlerGetter.get().serializeNBT(registries));
        if (isRitualActive()) {
            var recipe = currentRitualRecipe;
            assert recipe != null;
            nbt.putString("currentRitual", recipe.id().toString());
            if (!consumedIngredients.isEmpty()) {
                var list = new ListTag();
                for (var stack : consumedIngredients) {
                    list.add(stack.saveOptional(registries));
                }
                nbt.put("consumedIngredients", list);
            }
        }
        nbt.putInt("currentTime", currentTime);
        super.write(nbt, registries, clientPacket);
    }

    @Override
    public boolean isRitualActive() {
        if (blockEntity.getLevel() == null) return false;
        if (blockEntity.getLevel().isClientSide)
            return currentRitualRecipe != null;
        else
            return getRitualRecipe().isPresent();
    }

    @Override
    public int getTier() {
        return tier;
    }

    @Override
    public BehaviourType<?> getType() {
        return TYPE;
    }

    public void blockDirty() {
        blockEntity.notifyUpdate();
        if (blockEntity.getLevel() != null)
            blockEntity.getLevel().sendBlockUpdated(blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity.getBlockState(), 3);
    }
}
