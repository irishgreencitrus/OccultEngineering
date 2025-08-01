package io.github.irishgreencitrus.occultengineering.content.phlogiport;

import net.minecraft.core.BlockPos;

public interface IPhlogiportNetworkable {
    String getAddress();

    BlockPos getLocation();

    boolean isReceiving();

    boolean isFuelled();
}
