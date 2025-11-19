package io.github.irishgreencitrus.occultengineering.content.item.combinedgoggles;

import com.klikli_dev.occultism.common.item.armor.OtherworldGogglesItem;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.nbt.ByteTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraftforge.network.NetworkEvent;

public class ToggleCombinedGogglesPacket extends SimplePacketBase {

    public ToggleCombinedGogglesPacket() {}
    public ToggleCombinedGogglesPacket(FriendlyByteBuf buf) {}

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf) {

    }

    @Override
    public boolean handle(NetworkEvent.Context ctx) {
        var player = ctx.getSender();
        if (player == null) return false;
        player.server.execute(() -> {
            var headItem = player.getItemBySlot(EquipmentSlot.HEAD);

            // TODO: curios support
            if (!(headItem.getItem() instanceof CombinedGogglesItem)) return;

            // Maybe we should pass this from client -> server,
            //  but this also seems fairly infallible.
            var newState = !OtherworldGogglesItem.isGogglesItem(headItem);

            headItem.addTagElement(OtherworldGogglesItem.NBT_GOGGLES, ByteTag.valueOf(newState));
        });
        return true;
    }
}
