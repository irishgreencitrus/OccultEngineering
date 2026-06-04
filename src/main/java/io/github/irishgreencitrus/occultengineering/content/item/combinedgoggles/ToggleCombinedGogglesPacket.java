package io.github.irishgreencitrus.occultengineering.content.item.combinedgoggles;

import com.klikli_dev.occultism.common.item.armor.OtherworldGogglesItem;
import com.klikli_dev.occultism.registry.OccultismDataComponents;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringPackets;
import io.netty.buffer.ByteBuf;
import net.createmod.catnip.net.base.ServerboundPacketPayload;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import top.theillusivec4.curios.api.CuriosCapability;

public record ToggleCombinedGogglesPacket() implements ServerboundPacketPayload {
    public static final StreamCodec<ByteBuf, ToggleCombinedGogglesPacket> STREAM_CODEC = StreamCodec.unit(new ToggleCombinedGogglesPacket());

    @Override
    public void handle(ServerPlayer player) {
        player.server.execute(() -> {
            var headItem = player.getItemBySlot(EquipmentSlot.HEAD);

            if (headItem.getItem() instanceof CombinedGogglesItem) {
                // Maybe we should pass this from client -> server,
                //  but this also seems fairly infallible.
                var newState = !OtherworldGogglesItem.isGogglesItem(headItem);

                headItem.set(OccultismDataComponents.OTHERWORLD_GOGGLES, newState);
            } else {
                var curiosHandler = player.getCapability(CuriosCapability.INVENTORY);

                if (curiosHandler == null) {
                    return;
                }

                for (var stackHandler : curiosHandler.getCurios().values()) {
                    var dynamicStackHandler = stackHandler.getStacks();
                    for (int i = 0; i < dynamicStackHandler.getSlots(); i++) {

                        var stack = dynamicStackHandler.getStackInSlot(i);

                        if (stack.isEmpty() || (!(stack.getItem() instanceof CombinedGogglesItem))) continue;

                        var newState = !OtherworldGogglesItem.isGogglesItem(stack);
                        stack.set(OccultismDataComponents.OTHERWORLD_GOGGLES, newState);

                        return;
                    }
                }
            }
        });
    }

    @Override
    public PacketTypeProvider getTypeProvider() {
        return OccultEngineeringPackets.TOGGLE_COMBINED_GOGGLES;
    }
}
