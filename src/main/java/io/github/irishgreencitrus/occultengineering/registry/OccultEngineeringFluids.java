package io.github.irishgreencitrus.occultengineering.registry;

import com.tterrag.registrate.util.entry.FluidEntry;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import org.jetbrains.annotations.ApiStatus;

import static io.github.irishgreencitrus.occultengineering.OccultEngineering.REGISTRATE;

public class OccultEngineeringFluids {
    public static final FluidEntry<BaseFlowingFluid.Flowing> SPIRIT_SOLUTION =
            REGISTRATE
                    .standardFluid("spirit_solution")
                    .lang("Spirit Solution")
                    .tag(OccultEngineeringTags.SPIRIT_SOLUTION_FLUID)
                    .source(BaseFlowingFluid.Source::new)
                    .bucket()
                    .build()
                    .register();

    @ApiStatus.Internal
    public static void init() {
    }
}
