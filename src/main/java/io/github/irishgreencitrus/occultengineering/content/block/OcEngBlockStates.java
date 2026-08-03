package io.github.irishgreencitrus.occultengineering.content.block;

import net.minecraft.world.level.block.state.properties.IntegerProperty;

public final class OcEngBlockStates {
    public static final IntegerProperty TIER = IntegerProperty.create("tier", 1, 4);

    private OcEngBlockStates() {
    }
}
