package io.github.irishgreencitrus.occultengineering.kinetics.mechanicalArm;

import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPoint;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPointType;
import io.github.irishgreencitrus.occultengineering.block.MechanicalChamberBlock;
import io.github.irishgreencitrus.occultengineering.block.MechanicalChamberBlockEntity;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringTags;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class MechanicalChamberInteractionPoint extends ArmInteractionPointType {
    public MechanicalChamberInteractionPoint(ResourceLocation id) {
        super(id);
    }

    @Override
    public boolean canCreatePoint(Level level, BlockPos blockPos, BlockState blockState) {
        return blockState.getBlock() instanceof MechanicalChamberBlock;
    }

    @Nullable
    @Override
    public ArmInteractionPoint createPoint(Level level, BlockPos blockPos, BlockState blockState) {
        return new MechanicalChamberArmInteractionPoint(this, level, blockPos, blockState);
    }

    public static class MechanicalChamberArmInteractionPoint extends ArmInteractionPoint {

        public MechanicalChamberArmInteractionPoint(ArmInteractionPointType type, Level level, BlockPos pos, BlockState state) {
            super(type, level, pos, state);
        }

        @Override
        public ItemStack insert(ItemStack stack, boolean simulate) {
            if (stack.is(OccultEngineeringTags.MECHANICAL_CHAMBER_INSERTABLE)) {
                var be = level.getBlockEntity(this.pos);
                if (be == null) return stack;
                else if (be instanceof MechanicalChamberBlockEntity mbe) {
                    // You can only insert using a Mechanical Arm once the rest of the ritual is valid.
                    var ritual = mbe.getRitualFor(level, be.getBlockPos(), stack, null);
                    if (ritual.isPresent()) {
                        return super.insert(stack, simulate);
                    }
                }
            }
            return stack;
        }

        @Override
        public ItemStack extract(int slot, int amount, boolean simulate) {
            if (getHandler() == null) return ItemStack.EMPTY;
            if (getHandler().getStackInSlot(0).is(OccultEngineeringTags.MECHANICAL_CHAMBER_INSERTABLE)) {
                return ItemStack.EMPTY;
            } else {
                return super.extract(slot, amount, simulate);
            }
        }
    }
}
