package io.github.irishgreencitrus.occultengineering.content.fluid;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import io.github.irishgreencitrus.occultengineering.mixin.accessor.TankSegmentAccessor;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import java.util.function.Predicate;

public class FilteredFluidTankBehaviour extends SmartFluidTankBehaviour {
    protected final Predicate<FluidStack> fluidFilter;

    public FilteredFluidTankBehaviour(
            BehaviourType<SmartFluidTankBehaviour> type,
            Predicate<FluidStack> fluidFilter,
            SmartBlockEntity be,
            int tanks,
            int tankCapacity,
            boolean enforceVariety
    ) {
        super(type, be, tanks, tankCapacity, enforceVariety);
        this.fluidFilter = fluidFilter;

        IFluidHandler[] handlers = new IFluidHandler[tanks];
        for (int i = 0; i < tanks; i++) {
            TankSegment tankSegment = new TankSegment(tankCapacity);
            this.tanks[i] = tankSegment;
            handlers[i] = ((TankSegmentAccessor) tankSegment).getTank();
        }
        this.capability = new InternalFluidHandler(handlers, enforceVariety);
    }

    public static FilteredFluidTankBehaviour single(Predicate<FluidStack> fluidFilter, SmartBlockEntity be, int capacity) {
        return new FilteredFluidTankBehaviour(TYPE, fluidFilter, be, 1, capacity, false);
    }

    public class InternalFluidHandler extends SmartFluidTankBehaviour.InternalFluidHandler {

        public InternalFluidHandler(IFluidHandler[] handlers, boolean enforceVariety) {
            super(handlers, enforceVariety);
        }

        @Override
        public int fill(FluidStack resource, FluidAction action) {
            if (!fluidFilter.test(resource))
                return 0;
            return super.fill(resource, action);
        }
    }
}
