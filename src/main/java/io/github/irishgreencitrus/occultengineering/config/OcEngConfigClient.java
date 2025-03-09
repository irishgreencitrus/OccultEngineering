package io.github.irishgreencitrus.occultengineering.config;

import net.createmod.catnip.config.ConfigBase;
import net.minecraft.MethodsReturnNonnullByDefault;

@MethodsReturnNonnullByDefault
public class OcEngConfigClient extends ConfigBase {
    @Override
    public String getName() {
        return "client";
    }
}
