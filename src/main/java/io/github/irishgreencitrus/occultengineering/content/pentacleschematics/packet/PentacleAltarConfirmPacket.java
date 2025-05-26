package io.github.irishgreencitrus.occultengineering.content.pentacleschematics.packet;

import com.simibubi.create.foundation.networking.SimplePacketBase;
import io.github.irishgreencitrus.occultengineering.content.block.pentacle_altar.PentacleAltarBlockEntity;
import io.github.irishgreencitrus.occultengineering.content.block.pentacle_altar.PentacleAltarMenu;
import io.github.irishgreencitrus.occultengineering.content.item.PentacleSchematicItem;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.Objects;

public class PentacleAltarConfirmPacket extends SimplePacketBase {
    private String pentacleLocation;

    public PentacleAltarConfirmPacket(ResourceLocation location) {
        pentacleLocation = location.toString();
    }

    public PentacleAltarConfirmPacket(FriendlyByteBuf buffer) {
        // If the resource location is longer than 256 chars you
        // have bigger problems to worry about
        pentacleLocation = buffer.readUtf(256);
    }


    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeUtf(pentacleLocation);
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null) return;
            Level level = player.getCommandSenderWorld();
            BlockPos altarPos = ((PentacleAltarMenu) player.containerMenu).contentHolder
                    .getBlockPos();
            BlockEntity be = level.getBlockEntity(altarPos);
            if (be == null) return;
            if (be instanceof PentacleAltarBlockEntity altar) {
                altar.inventory.setStackInSlot(0, ItemStack.EMPTY);
                altar.inventory.setStackInSlot(1,
                        PentacleSchematicItem.create(Objects.requireNonNull(ResourceLocation.tryParse(pentacleLocation)))
                );
            }
        });
        return true;
    }
}
