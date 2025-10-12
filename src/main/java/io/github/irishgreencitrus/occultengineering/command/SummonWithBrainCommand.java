package io.github.irishgreencitrus.occultengineering.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.irishgreencitrus.occultengineering.content.entity.brain.DynamicBrainFactory;
import io.github.irishgreencitrus.occultengineering.content.entity.brain.DynamicBrainSupplantable;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringBrains;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;

import static net.minecraft.server.commands.SummonCommand.createEntity;

public class SummonWithBrainCommand {
    static ArgumentBuilder<CommandSourceStack, ?> register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
        return Commands.literal("summon")
                .requires(cs -> cs.hasPermission(2))
                .requires(CommandSourceStack::isPlayer)
                .then(Commands.argument("entity", ResourceArgument.resource(context, Registries.ENTITY_TYPE))
                        .suggests(SuggestionProviders.SUMMONABLE_ENTITIES)
                        .then(Commands.argument("brain",
                                ResourceLocationArgument.id()
                        ).executes((ctx) ->
                                spawnEntity(ctx.getSource(),
                                        ResourceArgument.getSummonableEntityType(ctx, "entity"),
                                        ResourceLocationArgument.getId(ctx, "brain"),
                                        ctx.getSource().getPosition(),
                                        new CompoundTag()))));
    }

    @SuppressWarnings("SameReturnValue")
    private static int spawnEntity(CommandSourceStack source, Holder.Reference<EntityType<?>> type, ResourceLocation brainLocation, Vec3 pos, CompoundTag tag) throws CommandSyntaxException {
        Entity entity = createEntity(source, type, pos, tag, true);
        if (!(entity instanceof DynamicBrainSupplantable)) {
            source.sendSuccess(() -> Component.literal("Created entity, but it does not have a dynamic brain"), true);
            return Command.SINGLE_SUCCESS;
        }
        DynamicBrainFactory<?> dynamicBrainFactory = OccultEngineeringBrains.REGISTRY.get(brainLocation);
        if (dynamicBrainFactory == null) {
            source.sendSuccess(() -> Component.literal("Created entity, but could not find brain " + brainLocation.toString()), true);
            return Command.SINGLE_SUCCESS;
        }
        var brain = dynamicBrainFactory.create(entity);

        if (brain == null) {
            source.sendSuccess(() -> Component.literal("Created entity, but the brain type " + brainLocation.toString() + " was invalid for this entity."), true);
            return Command.SINGLE_SUCCESS;
        }
        ((DynamicBrainSupplantable) entity).supplantBrain(brain);


        source.sendSuccess(() -> Component.translatable("commands.summon.success", entity.getDisplayName()), true);
        return Command.SINGLE_SUCCESS;
    }
}
