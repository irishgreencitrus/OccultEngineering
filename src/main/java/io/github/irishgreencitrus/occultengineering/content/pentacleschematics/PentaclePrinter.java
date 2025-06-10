package io.github.irishgreencitrus.occultengineering.content.pentacleschematics;

import com.google.common.collect.ImmutableList;
import com.klikli_dev.modonomicon.api.multiblock.Multiblock;
import com.klikli_dev.modonomicon.multiblock.matcher.TagMatcher;
import com.klikli_dev.occultism.common.block.ChalkGlyphBlock;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.simibubi.create.content.schematics.SchematicPrinter;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import io.github.irishgreencitrus.occultengineering.mixin.accessor.TagMatcherAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.NotImplementedException;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PentaclePrinter {

    private PentacleSchematic schematic;
    private BlockPos anchor;

    // If we have a block state matcher, we can simply place the block state we get from the state matcher.
    //  However, if we have a tag state matcher we have to work out what block state corresponds to based on what
    //  materials we have. (If we are using say a Creative Crate to supply the materials, just place whatever we
    //  get from the state matcher.)

    private List<BlockPos> positionToPrint;
    private int currentPosIndex = 0;
    private Map<BlockPos, Either<BlockState, TagKey<Block>>> toPlace = new HashMap<>();
    private boolean initialised;
    private Level level;

    public PentaclePrinter() {
        initialised = false;
    }

    public void initialise(PentacleSchematic schematic) {
        this.level = schematic.getLevel();
        this.schematic = schematic;
        this.anchor = schematic.position;
        var simResults = schematic.getCurrentSimulationResult().getSecond();


        // Let's put them in a nice order, so we don't place the blocks weirdly
        // Sort by Y first so we don't place blocks without support.
        positionToPrint = simResults
                .stream()
                .map(Multiblock.SimulateResult::getWorldPosition)
                .sorted((o1, o2) -> Comparator.comparingInt(Vec3i::getY)
                        .thenComparingInt(Vec3i::getZ)
                        .thenComparingInt(Vec3i::getX)
                        .compare(o1, o2)
                )
                .toList();

        for (Multiblock.SimulateResult result : simResults) {
            // I'm pretty sure the TagMatcher is the only one with funky behaviour here,
            //  but edit here if I'm proven wrong.
            if (result.getStateMatcher() instanceof TagMatcher tm) {
                var tagAccessor = (TagMatcherAccessor) tm;
                toPlace.put(result.getWorldPosition(), Either.right(tagAccessor.getTag().get()));
            } else {
                toPlace.put(result.getWorldPosition(), Either.left(result.getStateMatcher().getDisplayedState(0)));
            }
        }

        this.initialised = true;
    }

    public boolean isInitialised() {
        return initialised;
    }

    public void deinit() {
        initialised = false;
        level = null;
        schematic = null;
        anchor = null;
        toPlace.clear();
    }

    /// @return If a block was successfully placed
    public boolean placeNextBlock() {
        if (!hasNextPlace()) {
            deinit();
            return false;
        }

        var pos = positionToPrint.get(currentPosIndex++);

        assert toPlace.containsKey(pos);

        var nextToPlace = toPlace.get(pos);

        if (nextToPlace.left().isPresent()) {
            setBlock(level, pos, nextToPlace.left().get());
        } else {
            var tag = nextToPlace.right().get();
            ImmutableList<Block> all = ImmutableList.copyOf(ForgeRegistries.BLOCKS.tags().getTag(tag));

            if (all.isEmpty()) {
                return false;
            }

            var state = all.get(0).defaultBlockState();
            setBlock(level, pos, state);
        }
        return true;
    }

    public boolean hasNextPlace() {
        if (!isInitialised()) return false;
        return currentPosIndex < positionToPrint.size() && currentPosIndex >= 0;
    }

    public Either<BlockState, TagKey<Block>> getNextStateToPlace() {
        return toPlace.get(
                positionToPrint.get(currentPosIndex)
        );
    }

    public Pair<BlockPos, Either<BlockState, TagKey<Block>>> getNextToPlace() {
        var pos = positionToPrint.get(currentPosIndex);
        return Pair.of(pos, toPlace.get(pos));
    }

    public Pair<BlockPos, Either<BlockState, TagKey<Block>>> popNextToPlace() {
        var x = getNextToPlace();
        currentPosIndex++;
        return x;
    }

    Direction[] validHorizontalDirections = {Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};

    private void setBlock(Level level, BlockPos pos, BlockState state) {
        if (state.getBlock() instanceof ChalkGlyphBlock) {
            state = state.setValue(ChalkGlyphBlock.SIGN, level.getRandom().nextInt(ChalkGlyphBlock.MAX_SIGN + 1))
                    .setValue(BlockStateProperties.HORIZONTAL_FACING, validHorizontalDirections[level.getRandom().nextInt(validHorizontalDirections.length)]);
        }

        if (state.getBlock() instanceof CandleBlock) {
            state = state.setValue(CandleBlock.LIT, true);
        }

        level.destroyBlock(pos, true);
        level.setBlock(pos, state, Block.UPDATE_ALL);

        try {
            state.getBlock()
                    .setPlacedBy(level, pos, state, null, new ItemStack(state.getBlock()));
        } catch (Exception ignore) {
        }
    }

    public ItemRequirement getStillToPlaceRequrement() {
        if (!initialised) return ItemRequirement.INVALID;
        var newRequirement = ItemRequirement.NONE;
        var currentRequirement = schematic.getItemRequirement();

        // TODO: finish this
        //  we need to remove all the requirements that are filled by already placed blocks.
        for (var requirement : currentRequirement.getRequiredItems()) {
            currentRequirement = currentRequirement.union(new ItemRequirement(requirement));

        }
        return newRequirement;
    }

    public ItemRequirement getCurrentRequirement() {
        throw new NotImplementedException();
    }

    public int markBlockRequirements(PentacleMaterialChecklist checklist, Level world, SchematicPrinter.PlacementPredicate predicate) {
        throw new NotImplementedException();
    }

}
