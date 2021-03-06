package com.sunekaer.mods.toolkit.commands;

import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.minecraft.command.Commands.literal;

public class CommandKillAllAnimals {
    public static ArgumentBuilder<CommandSource, ?> register() {
        return literal("killAllAnimals")
                .requires(cs -> cs.hasPermissionLevel(2))
                        .executes(ctx -> remove(
                                ctx.getSource()
                        )
                );
    }

    private static int remove(CommandSource source) {
        World world = source.getWorld();
        AtomicInteger i = new AtomicInteger();

        source.sendFeedback(new TranslationTextComponent("commands.toolkit.killall.animals.start"), true);
        Stream<Entity> stream = ((ServerWorld) world).getEntities();

        stream.collect(Collectors.toList()).forEach(entity -> {
            if ((entity instanceof AnimalEntity)){
                i.addAndGet(1);
                entity.onKillCommand();
            }
        });

        if (i.get() > 0) {
            source.sendFeedback(new TranslationTextComponent("commands.toolkit.killall.animals.done", i.get()), true);
        } else {
            source.sendFeedback(new TranslationTextComponent("commands.toolkit.killall.animals.no"), true);
        }
        return 1;
    }
}

