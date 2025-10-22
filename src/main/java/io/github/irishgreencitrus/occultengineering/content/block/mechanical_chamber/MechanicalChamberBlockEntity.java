package io.github.irishgreencitrus.occultengineering.content.block.mechanical_chamber;

import com.klikli_dev.occultism.common.item.DummyTooltipItem;
import com.klikli_dev.occultism.crafting.recipe.RitualRecipe;
import com.klikli_dev.occultism.registry.OccultismRecipes;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringBlockEntities;
import net.createmod.catnip.lang.LangBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;


// TODO: rewrite to match how the Golden Sacrificial Bowl works in 1.21.1
// TODO: see if we can hijack the Golden Sacrificial Bowl rather than rewrite the whole class.
public class MechanicalChamberBlockEntity extends KineticBlockEntity {
    private @Nullable RitualProcessorBehaviour processorBehaviour;
    public ItemStackHandler itemStackHandler;
    public long lastFailedCheck = -1;
    public long lastChangeTime = 0;

    public MechanicalChamberBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        itemStackHandler = new ItemStackHandler(1) {
            private ItemStack handleDummyInsert(int slot, @NotNull ItemStack stack, boolean simulate) {
                var insertResult = super.insertItem(slot,stack,simulate);
                var activationItemStack = getStackInSlot(0);

                if (!simulate && insertResult.getCount() != stack.getCount() && stack.getItem() instanceof DummyTooltipItem activationItem) {
                    activationItem.performRitual(level, getBlockPos(), null, null, activationItemStack);
                    activationItemStack.shrink(1);
                }
                return insertResult;
            }

            @Override
            public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
                // Can't process it, there's no point in trying.
                if (level == null || processorBehaviour == null) return stack;

                if (stack.getItem() instanceof DummyTooltipItem)
                    return handleDummyInsert(slot,stack,simulate);

                if (processorBehaviour.isRitualActive()) return stack;

                if (MechanicalChamberBlockEntity.this.lastFailedCheck >= 0) {
                    long time = level.getGameTime();
                    long timeSinceLastFail = time - MechanicalChamberBlockEntity.this.lastFailedCheck;
                    if (timeSinceLastFail < 5 * 20) {
                        return stack;
                    }
                }

                var ritualRecipe = level.getRecipeManager().getAllRecipesFor(OccultismRecipes.RITUAL_TYPE.get()).stream().filter(
                        r -> r.value().matches(level, getBlockPos(), stack)
                ).findFirst().orElse(null);

                if (ritualRecipe == null) {
                    MechanicalChamberBlockEntity.this.lastFailedCheck = level.getGameTime();
                    return stack;
                }

                var insertResult = super.insertItem(slot, stack, simulate);
                var activationStack = getStackInSlot(0);

                if (!simulate) {
                    if (ritualRecipe.value().getRitual().isValid(level, getBlockPos(), null, null, activationStack, ritualRecipe.value().getIngredients() )) {
                        processorBehaviour.startRitual(null, activationStack, ritualRecipe);
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
                if (level == null) return;
                if (processorBehaviour != null)
                    processorBehaviour.blockDirty();
                else
                    setChanged();
                lastChangeTime = level.getGameTime();
            }
        };

    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                OccultEngineeringBlockEntities.MECHANICAL_CHAMBER.get(),
                (be, ctx) -> be.itemStackHandler
        );
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        processorBehaviour = new RitualProcessorBehaviour(
                this,
                () -> itemStackHandler,
                () -> {
                    // TODO: make this configurable.
                    var calc = (int) (Math.abs(getSpeed()) / 32f);
                    return calc <= 0 ? 1 : calc;
                }
        );
        behaviours.add(processorBehaviour);
    }

    public Optional<RecipeHolder<RitualRecipe>> getRitualFor(Level pLevel, ItemStack stack) {
        if (level == null || processorBehaviour == null) return Optional.empty();
        var ritualRecipe = pLevel.getRecipeManager().getAllRecipesFor(OccultismRecipes.RITUAL_TYPE.get()).stream().filter(
                r -> r.value().matches(pLevel, getBlockPos(), stack)
        ).findFirst().orElse(null);
        return Optional.ofNullable(ritualRecipe);
    }

    public static Component getPentacleName(ResourceLocation resourceLocation) {
        return Component.translatable("multiblock." + resourceLocation.getNamespace() + "." + resourceLocation.getPath());
    }

    private static LangBuilder lang() {
        return new LangBuilder(OccultEngineering.MODID);
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        var parent = super.addToGoggleTooltip(tooltip, isPlayerSneaking);
        if (processorBehaviour != null && processorBehaviour.isRitualActive()) {
            lang()
                    .translate("tooltip.ritualspeed")
                    .space()
                    .text(String.valueOf(processorBehaviour.ritualSpeedMultiplier.get()))
                    .text("x")
                    .style(ChatFormatting.GRAY).forGoggles(tooltip);

            assert processorBehaviour.currentRitualRecipe != null;
            if (!isPlayerSneaking) return parent;
            lang()
                    .translate("tooltip.currentpentacle")
                    .style(ChatFormatting.GRAY)
                    .forGoggles(tooltip);

            lang().space()
                    .add(getPentacleName(processorBehaviour.currentRitualRecipe.value().getPentacleId()))
                    .style(ChatFormatting.YELLOW).forGoggles(tooltip);

            lang()
                    .translate("tooltip.currentritualrecipe")
                    .style(ChatFormatting.GRAY)
                    .forGoggles(tooltip);
            // I promise there's nothing wrong with passing null here
            lang()
                    .space()
                    .add(processorBehaviour.currentRitualRecipe.value().getResultItem(null).getHoverName())
                    .style(ChatFormatting.GREEN)
                    .forGoggles(tooltip);
        } else {
            var builder = new LangBuilder(OccultEngineering.MODID);
            builder.translate("tooltip.ritualnotactive").style(ChatFormatting.RED).forGoggles(tooltip);
        }
        return parent;
    }
}