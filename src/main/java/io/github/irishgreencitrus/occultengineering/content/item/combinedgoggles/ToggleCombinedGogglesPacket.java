package io.github.irishgreencitrus.occultengineering.content.item.combinedgoggles;

import com.klikli_dev.occultism.common.item.armor.OtherworldGogglesItem;
import com.klikli_dev.occultism.registry.OccultismDataComponents;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringPackets;
import io.netty.buffer.ByteBuf;
import net.createmod.catnip.net.base.ServerboundPacketPayload;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;

public record ToggleCombinedGogglesPacket() implements ServerboundPacketPayload {
    public static final StreamCodec<ByteBuf, ToggleCombinedGogglesPacket> STREAM_CODEC = StreamCodec.unit(new ToggleCombinedGogglesPacket());

    @Override
    public void handle(ServerPlayer player) {
        player.server.execute(() -> {
            var headItem = player.getItemBySlot(EquipmentSlot.HEAD);

            // TODO: curios support
            if (!(headItem.getItem() instanceof CombinedGogglesItem)) return;

            // Maybe we should pass this from client -> server,
            //  but this also seems fairly infallible.
            var newState = !OtherworldGogglesItem.isGogglesItem(headItem);

            headItem.set(OccultismDataComponents.OTHERWORLD_GOGGLES, newState);
        });
    }

    @Override
    public PacketTypeProvider getTypeProvider() {
        return OccultEngineeringPackets.TOGGLE_COMBINED_GOGGLES;
    }
}
