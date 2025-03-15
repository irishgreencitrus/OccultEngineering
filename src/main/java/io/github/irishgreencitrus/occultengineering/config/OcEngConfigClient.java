package io.github.irishgreencitrus.occultengineering.config;

import net.createmod.catnip.config.ConfigBase;
import net.minecraft.MethodsReturnNonnullByDefault;

@MethodsReturnNonnullByDefault
public class OcEngConfigClient extends ConfigBase {
    public final ConfigGroup client = group(0, "client", Comments.client);
    public final ConfigFloat spiritSolutionTransparencyMultiplier = f(1, .125f, 256, "spirit_solution", Comments.spiritSolutionTransparencyMultiplier);

    @Override
    public String getName() {
        return "client";
    }

    private static class Comments {
        static String client = "Client-only settings";
        static String spiritSolutionTransparencyMultiplier = "The vision range through Spirit Solution will be multiplied by this factor";
    }
}
