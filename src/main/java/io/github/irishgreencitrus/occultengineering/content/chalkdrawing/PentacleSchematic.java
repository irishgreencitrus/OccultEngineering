package io.github.irishgreencitrus.occultengineering.content.chalkdrawing;

import com.klikli_dev.modonomicon.api.multiblock.Multiblock;
import com.mojang.datafixers.util.Pair;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;

import java.util.Collection;

public class PentacleSchematic {
    private Multiblock pentacle;
    private Level level;
    private BlockPos anchor;
    private Rotation rotation;
    private Pair<BlockPos, Collection<Multiblock.SimulateResult>> simulationResults;

    public PentacleSchematic(Level level, Multiblock pentacle, BlockPos anchor, Rotation rotation) {
        this.pentacle = pentacle;
        this.level = level;
        this.anchor = anchor;
        this.rotation = rotation;
    }

    public void populateSimulation() {
        simulationResults = pentacle.simulate(level, anchor, rotation, false, false);
    }

    public ItemRequirement getItemRequirement() {
        ItemRequirement itemRequirement = ItemRequirement.NONE;
        if (simulationResults == null || simulationResults.getSecond().isEmpty()) {
            populateSimulation();
        }
        if (simulationResults.getSecond().isEmpty()) {
            return ItemRequirement.INVALID;
        }

        simulationResults.getSecond().forEach(r -> {
            var targetState = r.getStateMatcher().getDisplayedState(level.getGameTime()).rotate(rotation);
            var requirement = ItemRequirement.of(targetState, null);
            itemRequirement.union(requirement);
        });

        return itemRequirement;
    }

}
