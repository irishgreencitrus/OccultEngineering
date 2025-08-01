package io.github.irishgreencitrus.occultengineering.content.phlogiport;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class PhlogiportLinkBehaviour extends BlockEntityBehaviour implements IPhlogiportNetworkable {
    public static final BehaviourType<PhlogiportLinkBehaviour> TYPE = new BehaviourType<>();

    boolean acceptsPackages;
    String address;

    public PhlogiportLinkBehaviour(SmartBlockEntity be) {
        super(be);
    }

    public void update(String address, boolean acceptsPackages) {
        if (address == null || address.isBlank()) return;
        if (Objects.equals(this.address, address) && this.acceptsPackages == acceptsPackages) return;

        getHandler().removeFromNetwork(getWorld(), this);
        this.acceptsPackages = acceptsPackages;
        this.address = address;
        getHandler().addToNetwork(getWorld(), this);
    }

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public BlockPos getLocation() {
        return getPos();
    }

    @Override
    public boolean isReceiving() {
        return acceptsPackages;
    }

    @Override
    public boolean isFuelled() {
        return true;
    }

    @Override
    public BehaviourType<?> getType() {
        return TYPE;
    }

    @Override
    public void initialize() {
        super.initialize();
        if (getWorld().isClientSide()) return;
        getHandler().addToNetwork(getWorld(), this);
    }

    @Override
    public void unload() {
        super.unload();
        if (getWorld().isClientSide()) return;
        getHandler().removeFromNetwork(getWorld(), this);
    }

    public @Nullable IPhlogiportNetworkable getMatchingPhlogiport(String destinationAddress) {
        return getHandler().findMatchingPhlogiport(getWorld(), this, destinationAddress);
    }

    private PhlogiportNetworkHandler getHandler() {
        return OccultEngineering.PHLOGIPORT_NETWORK;
    }
}
