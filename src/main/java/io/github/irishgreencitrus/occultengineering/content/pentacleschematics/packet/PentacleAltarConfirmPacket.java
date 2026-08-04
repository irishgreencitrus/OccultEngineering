package io.github.irishgreencitrus.occultengineering.content.pentacleschematics.packet;

import io.github.irishgreencitrus.occultengineering.content.block.pentacle_altar.PentacleAltarBlockEntity;
import io.github.irishgreencitrus.occultengineering.content.block.pentacle_altar.PentacleAltarMenu;
import io.github.irishgreencitrus.occultengineering.content.item.PentacleSchematicItem;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringPackets;
import io.netty.buffer.ByteBuf;
import net.createmod.catnip.net.base.ServerboundPacketPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Objects;

public record PentacleAltarConfirmPacket(ResourceLocation pentacleLocation) implements ServerboundPacketPayload {
    public static final StreamCodec<ByteBuf, PentacleAltarConfirmPacket> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC, PentacleAltarConfirmPacket::pentacleLocation,
            PentacleAltarConfirmPacket::new
    );

    @Override
    public void handle(ServerPlayer player) {
        player.server.executeBlocking(() -> {
            Level level = player.getCommandSenderWorld();
            BlockPos altarPos = ((PentacleAltarMenu) player.containerMenu).contentHolder
                    .getBlockPos();
            BlockEntity be = level.getBlockEntity(altarPos);
            if (be == null) return;
            if (be instanceof PentacleAltarBlockEntity altar) {
                altar.inventory.setStackInSlot(0, ItemStack.EMPTY);
                altar.inventory.setStackInSlot(1,
                        PentacleSchematicItem.create(Objects.requireNonNull(pentacleLocation))
                );
            }
        });
    }

    @Override
    public PacketTypeProvider getTypeProvider() {
        return OccultEngineeringPackets.PENTACLE_ALTAR_CONFIRM;
    }
}
