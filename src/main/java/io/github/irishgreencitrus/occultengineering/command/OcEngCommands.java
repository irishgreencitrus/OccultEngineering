package io.github.irishgreencitrus.occultengineering.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.world.entity.player.Player;

import java.util.function.Predicate;

public class OcEngCommands {
    public static final Predicate<CommandSourceStack> SOURCE_IS_PLAYER = cs -> cs.getEntity() instanceof Player;

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("occultengineering")
                .then(MultiblockCommand.register());
        dispatcher.register(root);
    }
}
