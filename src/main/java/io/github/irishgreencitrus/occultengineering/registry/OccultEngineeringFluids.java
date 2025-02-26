package io.github.irishgreencitrus.occultengineering.registry;

import com.tterrag.registrate.util.entry.FluidEntry;
import net.minecraftforge.fluids.ForgeFlowingFluid;

import static io.github.irishgreencitrus.occultengineering.OccultEngineering.REGISTRATE;

public class OccultEngineeringFluids {
    public static final FluidEntry<ForgeFlowingFluid.Flowing> SPIRIT_SOLUTION =
            REGISTRATE
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
