package io.github.irishgreencitrus.occultengineering.content.phlogiport.packet;

import com.simibubi.create.foundation.networking.SimplePacketBase;
import io.github.irishgreencitrus.occultengineering.content.phlogiport.PhlogiportBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

public class PhlogiportSendEffectPacket extends SimplePacketBase {
    private final BlockPos position;
    private final boolean isReceiver;
    private final boolean success;

    public PhlogiportSendEffectPacket(BlockPos position, boolean isReceiver, boolean success) {
        this.position = position;
        this.isReceiver = isReceiver;
        this.success = success;
    }

    public PhlogiportSendEffectPacket(FriendlyByteBuf buf) {
        position = buf.readBlockPos();
        isReceiver = buf.readBoolean();
        success = buf.readBoolean();
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeBlockPos(position);
        buf.writeBoolean(isReceiver);
        buf.writeBoolean(success);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            var level = Minecraft.getInstance().level;
            if (level == null) return;
            if (level.getBlockEntity(position) instanceof PhlogiportBlockEntity pbe) {
                pbe.playEffect(isReceiver, success);

            }
        });
        return true;
    }
}
