package io.github.irishgreencitrus.occultengineering.content.block.otherworld_detector.packet;

import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

public class ThirdEyeActivationPacket extends SimplePacketBase {
    public boolean isActive;
    public static final String DATA_ID = "occultengineering:third_eye_active";

    public ThirdEyeActivationPacket(boolean isActive) {
        this.isActive = isActive;
    }

    public ThirdEyeActivationPacket(FriendlyByteBuf buf) {
        this(buf.readBoolean());
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeBoolean(isActive);
    }

    public static boolean isActive(Player player) {
        return player.getPersistentData().getBoolean(DATA_ID);
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            var player = context.getSender();
            if (player == null) return;
            player.getPersistentData().putBoolean(DATA_ID, isActive);
        });
        return true;
    }
}
