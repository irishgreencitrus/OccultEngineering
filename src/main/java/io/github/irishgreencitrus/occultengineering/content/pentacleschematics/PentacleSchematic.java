package io.github.irishgreencitrus.occultengineering.content.pentacleschematics;

import com.klikli_dev.modonomicon.api.multiblock.Multiblock;
import com.klikli_dev.modonomicon.data.MultiblockDataManager;
import com.klikli_dev.modonomicon.multiblock.matcher.TagMatcher;
import com.mojang.datafixers.util.Either;
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
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class PentacleSchematic {
    private final Multiblock pentacle;
    private final Level level;
    public final BlockPos position;
    private Pair<BlockPos, Collection<Multiblock.SimulateResult>> simulationResults;

    public enum ParseResult {
        OK,
        NOT_PENTACLE_SCHEMATIC,
        NO_TAG,
        INVALID_TAG,
        NOT_DEPLOYED,
        INVALID_PENTACLE_NAME,
        INVALID_PENTACLE_MULTIBLOCK,
    }

    // The poor man's version of Result<T, E>
    public static @NotNull Either<PentacleSchematic, ParseResult> fromStack(Level level, ItemStack stack) {
        if (!OccultEngineeringItems.PENTACLE_SCHEMATIC.isIn(stack))
            return Either.right(ParseResult.NOT_PENTACLE_SCHEMATIC);
        if (!stack.hasTag()) return Either.right(ParseResult.NO_TAG);
        assert stack.getTag() != null;
        var t = stack.getTag();

        if (!(t.contains("Deployed")
                && t.contains("Pentacle")
                && t.contains("Position")
                && t.contains("Bounds")
        )) {
            return Either.right(ParseResult.INVALID_TAG);
        }

        var hasDeployed = t.getBoolean("Deployed");
        if (!hasDeployed) return Either.right(ParseResult.NOT_DEPLOYED);

        var pentacleName = stack.getTag().getString("Pentacle");
        var pentacleResource = ResourceLocation.tryParse(pentacleName);
        if (pentacleResource == null) return Either.right(ParseResult.INVALID_PENTACLE_NAME);

        var multiblock = MultiblockDataManager.get().getMultiblock(pentacleResource);
        if (multiblock == null) return Either.right(ParseResult.INVALID_PENTACLE_MULTIBLOCK);

        var pentaclePos = NbtUtils.readBlockPos(stack.getTag().getCompound("Position"));

        return Either.left(new PentacleSchematic(level, multiblock, pentaclePos));
    }

    public PentacleSchematic(Level level, Multiblock pentacle, BlockPos position) {
        this.pentacle = pentacle;
        this.level = level;
        this.position = position;
    }

    public Level getLevel() {
        return level;
    }

    public void populateSimulation() {
        var results = pentacle.simulate(level, position, Rotation.NONE, false, false);

        // If it doesn't count towards the total blocks, we don't need to place it.
        // e.g. all pentacles in Occultism have an Otherstone-Stone checkerboard base pattern
        //      which is just for viewing in the Dictionary.
        simulationResults = Pair.of(
                results.getFirst(),
                results.getSecond()
                        .stream()
                        .filter(i -> i.getStateMatcher().countsTowardsTotalBlocks())
                        .toList()
        );
    }

    public Pair<BlockPos, Collection<Multiblock.SimulateResult>> getCurrentSimulationResult() {
        populateSimulation();
        return simulationResults;
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
            var targetState = r.getStateMatcher().getDisplayedState(level.getGameTime());
            if (r.getStateMatcher() instanceof TagMatcher tagMatcher) {
                var tagAccess = (TagMatcherAccessor) tagMatcher;

                // We can get away with using a block tag as the items have to be able to be placed in order to even get here.
                var tagRequirement = new BlockTagRequirement(
                        new ItemStack(tagMatcher.getDisplayedState(level.getGameTime()).getBlock()),
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
        var printer = new PentaclePrinter();
        printer.initialise(this);
        if (printer.isInitialised())
            printer.placeAll();
    }

}
