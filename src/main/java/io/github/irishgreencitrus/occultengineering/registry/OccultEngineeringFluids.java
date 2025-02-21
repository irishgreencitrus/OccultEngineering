package io.github.irishgreencitrus.occultengineering.registry;

import com.tterrag.registrate.util.entry.FluidEntry;
import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import net.minecraftforge.fluids.ForgeFlowingFluid;

public class OccultEngineeringFluids {
    public static final FluidEntry<ForgeFlowingFluid.Flowing> SPIRIT_SOLUTION =
            OccultEngineering
                    .REGISTRATE
                    .standardFluid("spirit_solution")
                    .lang("Spirit Solution")
                    .tag(OccultEngineeringTags.SPIRIT_SOLUTION_FLUID)
                    .source(ForgeFlowingFluid.Source::new)
                    .bucket()
                    .build()
                    .register();
    public static void register() {
    }
}
