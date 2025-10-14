package io.github.irishgreencitrus.occultengineering.content.block.phlogiport;

import net.minecraft.core.BlockPos;

public interface IPhlogiportNetworkable {
    String getAddress();

    BlockPos getLocation();

    boolean isReceiving();

    boolean isFuelled();
}
