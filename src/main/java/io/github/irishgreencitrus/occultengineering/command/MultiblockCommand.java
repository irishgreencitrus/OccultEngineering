package io.github.irishgreencitrus.occultengineering.command;

import com.klikli_dev.modonomicon.data.MultiblockDataManager;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;

public class MultiblockCommand {
    static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("multiblock")
                .requires(cs -> cs.hasPermission(2))
                .requires(CommandSourceStack::isPlayer)
                .then(Commands.literal("place")
                        .then(Commands.argument("id", ResourceLocationArgument.id())
                                .then(Commands.argument("location", BlockPosArgument.blockPos())
                                        .executes(ctx ->
                                                run(ctx, ResourceLocationArgument.getId(ctx, "id"),
                                                        BlockPosArgument.getLoadedBlockPos(ctx, "location"),
                                                        ctx.getSource().getLevel())))));
    }

    private static int run(CommandContext<CommandSourceStack> ctx, ResourceLocation location, BlockPos blockPos, Level level) {
        var multiblock = MultiblockDataManager.get().getMultiblock(location);
        multiblock.place(level, blockPos, Rotation.NONE);
        return Command.SINGLE_SUCCESS;
    }
}
