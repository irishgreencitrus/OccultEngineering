package io.github.irishgreencitrus.occultengineering.content.phlogiport;

import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.logistics.box.PackageItem;
import com.simibubi.create.content.logistics.packagePort.PackagePortBlockEntity;
import com.simibubi.create.content.logistics.packagerLink.WiFiParticle;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import io.github.irishgreencitrus.occultengineering.config.OccultEngineeringConfig;
import io.github.irishgreencitrus.occultengineering.content.phlogiport.packet.PhlogiportSendEffectPacket;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringPackets;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.items.ItemHandlerHelper;

import java.util.List;
import java.util.Objects;

public class PhlogiportBlockEntity extends PackagePortBlockEntity {
    private PhlogiportLinkBehaviour link;
    private boolean inventoryFull = false;

    public PhlogiportBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        target = null; // We don't use the target, but we use everything else.
    }

    private boolean shouldAcceptPackage() {
        return !inventoryFull;
    }

    @Override
    public void filterChanged() {
        super.filterChanged();
        link.update(addressFilter, shouldAcceptPackage());
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
        link.update(addressFilter, shouldAcceptPackage());
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

        var inventoryFull = true;
        for (int i = 0; i < inventory.getSlots(); i++) {
            var stack = inventory.extractItem(i, 1, true);

            if (stack.isEmpty()) {
                inventoryFull = false;
                continue;
            }

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

                    inventoryFull = false;

                    var distance = worldPosition.distManhattan(pbe.worldPosition);

                    // Offset it slightly so it looks like the signal is coming from the antenna
                    var signalCenter = worldPosition.getCenter().add(PhlogiportSignalParticle.offset);

                    // 3 blocks per tick
                    var receivePackageTimer = distance / 3;

                    serverLevel.sendParticles(
                            new PhlogiportSignalParticleData(new BlockPositionSource(pbe.getBlockPos()), receivePackageTimer),
                            signalCenter.x(),
                            signalCenter.y(),
                            signalCenter.z(),
                            1, 0, 0, 0, 1);

                    CatnipServices.NETWORK.sendToClientsAround(serverLevel, worldPosition, OccultEngineeringConfig.server().phlogiportRangeBlocks.get(), new PhlogiportSendEffectPacket(worldPosition, false, true));
                    CatnipServices.NETWORK.sendToClientsAround(serverLevel, pbe.worldPosition, OccultEngineeringConfig.server().phlogiportRangeBlocks.get(), new PhlogiportSendEffectPacket(pbe.worldPosition, true, true));

                    level.blockEntityChanged(worldPosition);
                    level.blockEntityChanged(pbe.worldPosition);
                }
            } else continue;

            // We did it, don't send another package till the next lazyTick()
            break;
        }

        if (inventoryFull != this.inventoryFull) {
            this.inventoryFull = inventoryFull;
            link.update(addressFilter, shouldAcceptPackage());
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
            AllSoundEvents.STOCK_LINK.playAt(level, pos, 0.5F, 2.0F, false);
            clientLevel.addParticle(new WiFiParticle.Data(), pos.x, pos.y, pos.z, 1, 1, 1);
        } else {
            AllSoundEvents.STOCK_LINK.playAt(clientLevel, pos, 0.5f, 2.0f, false);
            clientLevel.addParticle(new WiFiParticle.Data(), pos.x, pos.y, pos.z, 1, 1, 1);
        }
    }
}
