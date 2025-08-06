package io.github.irishgreencitrus.occultengineering.config;

import net.createmod.catnip.config.ConfigBase;
import net.minecraft.MethodsReturnNonnullByDefault;

@MethodsReturnNonnullByDefault
public class OcEngConfigServer extends ConfigBase {
    public final OcEngStress stressValues = nested(1, OcEngStress::new, Comments.stress);
    public final ConfigInt phlogiportRangeBlocks = i(128, 8, 2048, "Phlogiport Range", Comments.phlogiportRange);

    @Override
    public String getName() {
        return "server";
    }

    private static class Comments {
        static String stress = "Fine tune the kinetic stats of individual components";
        static String phlogiportRange = "The maximum distance a Phlogiport can forward a package, in blocks";
    }
}
