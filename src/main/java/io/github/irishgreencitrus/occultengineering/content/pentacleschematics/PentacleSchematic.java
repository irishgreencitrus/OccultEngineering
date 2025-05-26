package io.github.irishgreencitrus.occultengineering.content.pentacleschematics;

import com.klikli_dev.modonomicon.api.multiblock.Multiblock;
import com.klikli_dev.modonomicon.data.MultiblockDataManager;
import com.klikli_dev.modonomicon.multiblock.matcher.TagMatcher;
import com.mojang.datafixers.util.Pair;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import io.github.irishgreencitrus.occultengineering.mixin.accessor.TagMatcherAccessor;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringItems;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;

import java.util.Collection;
import java.util.Optional;

public class PentacleSchematic {
    private Multiblock pentacle;
    private Level level;
    private BlockPos position;
    private Pair<BlockPos, Collection<Multiblock.SimulateResult>> simulationResults;

    public static Optional<PentacleSchematic> fromStack(Level level, ItemStack stack) {
        if (!OccultEngineeringItems.PENTACLE_SCHEMATIC.isIn(stack)) return Optional.empty();
        if (!stack.hasTag()) return Optional.empty();
        assert stack.getTag() != null;

        var pentacleName = stack.getTag().getString("Pentacle");
        var pentacleResource = ResourceLocation.tryParse(pentacleName);
        if (pentacleResource == null) return Optional.empty();

        var multiblock = MultiblockDataManager.get().getMultiblock(pentacleResource);
        if (multiblock == null) return Optional.empty();

        var pentaclePos = NbtUtils.readBlockPos(stack.getTag().getCompound("Position"));

        return Optional.of(new PentacleSchematic(level, multiblock, pentaclePos));
    }

    public PentacleSchematic(Level level, Multiblock pentacle, BlockPos position) {
        this.pentacle = pentacle;
        this.level = level;
        this.position = position;
    }

    public void populateSimulation() {
        simulationResults = pentacle.simulate(level, position, Rotation.NONE, false, false);
    }

    public ItemRequirement getItemRequirement() {
        ItemRequirement itemRequirement = ItemRequirement.NONE;
        if (simulationResults == null || simulationResults.getSecond().isEmpty()) {
            populateSimulation();
        }
        if (simulationResults.getSecond().isEmpty()) {
            OccultEngineering.LOGGER.warn("Empty multiblock encountered");
            return ItemRequirement.INVALID;
        }

        for (Multiblock.SimulateResult r : simulationResults.getSecond()) {
            if (!r.getStateMatcher().countsTowardsTotalBlocks()) continue;
            var targetState = r.getStateMatcher().getDisplayedState(level.getGameTime());
            if (r.getStateMatcher() instanceof TagMatcher tagMatcher) {
                var tagAccess = (TagMatcherAccessor) tagMatcher;
                OccultEngineering.LOGGER.info("Need to match {}", tagAccess.getTag().get().toString());

                // We can get away with using a block tag as the items have to be able to be placed in order to even get here.
                var tagRequirement = new BlockTagRequirement(
                        new ItemStack(tagMatcher.getDisplayedState(level.getGameTime()).getBlock()),
                        ItemRequirement.ItemUseType.CONSUME,
                        tagAccess.getTag().get()
                );
                itemRequirement = itemRequirement.union(new ItemRequirement(tagRequirement));
            } else {
                var requirement = ItemRequirement.of(targetState, null);
                itemRequirement = itemRequirement.union(requirement);
            }

        }

        return itemRequirement;
    }

    public void instantPlace() {
        pentacle.place(level, position, Rotation.NONE);
    }

}
