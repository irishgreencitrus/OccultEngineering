package io.github.irishgreencitrus.occultengineering.content.item.combinedgoggles;

import com.klikli_dev.occultism.common.item.armor.OtherworldGogglesItem;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.nbt.ByteTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import top.theillusivec4.curios.api.CuriosCapability;

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

            if (headItem.getItem() instanceof CombinedGogglesItem) {
                toggle(headItem);
                return;
            }

            player.getCapability(CuriosCapability.INVENTORY).ifPresent(curiosHandler -> {
                for (var stackHandler : curiosHandler.getCurios().values()) {
                    var stacks = stackHandler.getStacks();
                    for (int slot = 0; slot < stacks.getSlots(); slot++) {
                        var stack = stacks.getStackInSlot(slot);
                        if (!(stack.getItem() instanceof CombinedGogglesItem)) continue;

                        toggle(stack);
                        return;
                    }
                }
            });
        });
        return true;
    }

    private static void toggle(ItemStack stack) {
        // Maybe we should pass this from client -> server,
        // but deriving the state here keeps the packet stateless.
        var newState = !OtherworldGogglesItem.isGogglesItem(stack);
        stack.addTagElement(OtherworldGogglesItem.NBT_GOGGLES, ByteTag.valueOf(newState));
    }
}
