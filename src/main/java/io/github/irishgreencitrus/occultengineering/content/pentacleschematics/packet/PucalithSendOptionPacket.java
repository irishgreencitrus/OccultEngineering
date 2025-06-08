package io.github.irishgreencitrus.occultengineering.content.pentacleschematics.packet;

import com.simibubi.create.foundation.networking.SimplePacketBase;
import io.github.irishgreencitrus.occultengineering.OccultEngineering;
import io.github.irishgreencitrus.occultengineering.content.block.pucalith.PucalithBlockEntity;
import io.github.irishgreencitrus.occultengineering.content.block.pucalith.PucalithMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class PucalithSendOptionPacket extends SimplePacketBase {
    public enum Option {
        PLAY,
        PAUSE,
        STOP
    }

    private final Option option;

    public PucalithSendOptionPacket(Option option) {
        this.option = option;
    }

    public PucalithSendOptionPacket(FriendlyByteBuf buf) {
        this(buf.readEnum(Option.class));
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeEnum(option);
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            var player = context.getSender();
            if (player == null || !(player.containerMenu instanceof PucalithMenu))
                return;
            PucalithBlockEntity be = ((PucalithMenu) player.containerMenu).contentHolder;
            switch (option) {
                case PLAY -> be.onPlayButton();
                case PAUSE -> be.onPauseButton();
                case STOP -> be.onStopButton();
                default -> OccultEngineering.LOGGER.warn("Got an unexpected Pucalith option of {}", option);
            }
            be.sendUpdate = true;
        });
        return true;
    }
}
