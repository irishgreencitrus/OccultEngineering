package io.github.irishgreencitrus.occultengineering.content.phlogiport;

import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.logistics.box.PackageItem;
import com.simibubi.create.content.logistics.packagePort.PackagePortBlockEntity;
import com.simibubi.create.content.logistics.packagerLink.WiFiParticle;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import io.github.irishgreencitrus.occultengineering.content.phlogiport.packet.PhlogiportSendEffectPacket;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringPackets;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.List;
import java.util.Objects;

public class PhlogiportBlockEntity extends PackagePortBlockEntity {
    public PhlogiportBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        target = null; // We don't use the target, but we use everything else.
    }

    private PhlogiportLinkBehaviour link;

    /*
        When I recieve a package, notify the PhlogiportNetworkHandler.
        If it can forward to the next Phlogiport it will.
        Phlogiports always forward to a more specific address.
        i.e. If we have a package named "ABCD",
        we can start at a phlogiport named "*"
        which will then forward (->) to the phlogiport named "A*"
        -> AB* -> ABC* -> ABCD (final destination)

        This could be used to make a crude addressing and sorting system, but is in general much more useful
        than having greedy addresses (which would work the opposite way)
     */

    @Override
    public void filterChanged() {
        super.filterChanged();
        link.update(addressFilter, acceptsPackages);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        link = new PhlogiportLinkBehaviour(this);
        behaviours.add(link);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (level == null) return;
        if (level.isClientSide()) return;
        link.update(addressFilter, acceptsPackages);
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        if (level == null) return;
        if (level.isClientSide()) return;

        trySendingPackage();
    }

    protected void trySendingPackage() {
        if (level == null) return;

        for (int i = 0; i < inventory.getSlots(); i++) {
            var stack = inventory.extractItem(i, 1, true);
            if (stack.isEmpty()) continue;
            if (!PackageItem.isPackage(stack)) continue;

            var address = PackageItem.getAddress(stack);

            if (address.isEmpty()) continue;
            if (Objects.equals(address, this.addressFilter)) continue;

            var matchingPort = link.getMatchingPhlogiport(address);
            if (matchingPort == null) continue;

            var be = level.getBlockEntity(matchingPort.getLocation());
            if (be == null) continue;

            if (be instanceof PhlogiportBlockEntity pbe) {
                var remainder = ItemHandlerHelper.insertItemStacked(pbe.inventory, stack, false);
                var serverLevel = (ServerLevel) level;


                if (remainder.isEmpty()) {
                    inventory.extractItem(i, 1, false);

                    OccultEngineeringPackets.sendToNear(serverLevel, worldPosition, 64, new PhlogiportSendEffectPacket(worldPosition, false, true));
                    OccultEngineeringPackets.sendToNear(serverLevel, pbe.worldPosition, 64, new PhlogiportSendEffectPacket(pbe.worldPosition, true, true));

                    level.blockEntityChanged(worldPosition);
                }
            } else continue;

            // We did it, don't send another package till the next lazyTick()
            break;
        }
    }

    @Override
    protected void onOpenChange(boolean b) {
    }

    @OnlyIn(Dist.CLIENT)
    public void playEffect(boolean isReceiver, boolean success) {
        if (level == null || !level.isClientSide) return;
        var clientLevel = (ClientLevel) level;

        var pos = Vec3.atCenterOf(worldPosition);

        if (isReceiver) {
            AllSoundEvents.FROGPORT_DEPOSIT.playAt(level, pos, 0.5F, 1.0F, false);
            clientLevel.addParticle(ParticleTypes.END_ROD, pos.x, pos.y, pos.z, 1, 1, 1);
        } else {
            AllSoundEvents.STOCK_LINK.playAt(clientLevel, pos, 0.5f, 2.0f, false);
            clientLevel.addParticle(new WiFiParticle.Data(), pos.x, pos.y, pos.z, 1, 1, 1);
        }
    }
}
