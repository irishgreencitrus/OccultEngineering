package io.github.irishgreencitrus.occultengineering.content.phlogiport;

import com.simibubi.create.content.logistics.box.PackageItem;
import com.simibubi.create.content.logistics.packagePort.PackagePortBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.List;

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
        link.update(addressFilter, acceptsPackages);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        link = new PhlogiportLinkBehaviour(this);
        behaviours.add(link);
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

        var inventory = itemHandler.resolve().orElse(null);
        if (inventory == null) return;

        for (int i = 0; i < inventory.getSlots(); i++) {
            var stack = inventory.extractItem(i, 1, true);
            if (stack.isEmpty()) continue;
            if (PackageItem.isPackage(stack)) continue;

            var address = PackageItem.getAddress(stack);
            if (address.isEmpty()) continue;

            var matchingPort = link.getMatchingPhlogiport(address);
            if (matchingPort == null) continue;

            var be = level.getBlockEntity(matchingPort.getLocation());
            if (be == null) continue;

            if (be instanceof PhlogiportBlockEntity pbe) {
                var remainder = ItemHandlerHelper.insertItemStacked(pbe.inventory, stack, false);
                if (remainder.isEmpty()) {
                    inventory.extractItem(i, 1, false);
                    level.blockEntityChanged(worldPosition);
                }
            } else continue;

            // We did it!
            OccultEngineering.LOGGER.info("Inserted an item to a Phlogiport!");
            break;
        }


    }

    @Override
    protected void onOpenChange(boolean b) {
    }
}
