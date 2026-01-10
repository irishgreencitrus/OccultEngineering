package io.github.irishgreencitrus.occultengineering.content.block;

import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class OcEngBlockStates {
    private OcEngBlockStates() {}
    public static final IntegerProperty TIER = IntegerProperty.create("tier", 1, 4);
}
