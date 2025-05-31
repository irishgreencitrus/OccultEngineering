package io.github.irishgreencitrus.occultengineering.content.pentacleschematics;

import com.google.common.collect.ImmutableList;
import com.klikli_dev.modonomicon.api.multiblock.Multiblock;
import com.klikli_dev.modonomicon.multiblock.matcher.TagMatcher;
import com.klikli_dev.occultism.common.block.ChalkGlyphBlock;
import com.simibubi.create.content.schematics.SchematicPrinter;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import io.github.irishgreencitrus.occultengineering.mixin.accessor.TagMatcherAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
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
    private Map<BlockPos, BlockState> blocksToPlace = new HashMap<>();
    private Map<BlockPos, TagKey<Block>> tagsToPlace = new HashMap<>();
    private boolean initialised = false;
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
                tagsToPlace.put(result.getWorldPosition(), tagAccessor.getTag().get());
            } else {
                blocksToPlace.put(result.getWorldPosition(), result.getStateMatcher().getDisplayedState(0));
            }
        }

        this.initialised = true;
    }

    public boolean isInitialised() {
        return initialised;
    }

    private int tickCount = 0;

    public void serverTick() {
        if (tickCount % 20 == 0) {
            placeNextBlock();
            tickCount = 0;
        }
        tickCount++;
    }

    /// @return If a block was successfully placed
    public boolean placeNextBlock() {
        if (currentPosIndex >= positionToPrint.size()) {
            this.initialised = false;
            return false;
        }

        var pos = positionToPrint.get(currentPosIndex);
        currentPosIndex++;
        if (blocksToPlace.containsKey(pos)) {
            var state = blocksToPlace.get(pos);

            setBlock(level, pos, state);
            return true;

        } else if (tagsToPlace.containsKey(pos)) {
            var tag = tagsToPlace.get(pos);
            ImmutableList<Holder<Block>> all = ImmutableList.copyOf(BuiltInRegistries.BLOCK.getTagOrEmpty(tag));

            if (all.isEmpty()) {
                return false;
            }

            var state = all.get(0).get().defaultBlockState();
            setBlock(level, pos, state);
            return true;
        }

        if (currentPosIndex == positionToPrint.size() - 1)
            this.initialised = false;

        return false;
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

    public ItemRequirement getCurrentRequirement() {
        throw new NotImplementedException();
    }

    public int markBlockRequirements(PentacleMaterialChecklist checklist, Level world, SchematicPrinter.PlacementPredicate predicate) {
        throw new NotImplementedException();
    }

}
