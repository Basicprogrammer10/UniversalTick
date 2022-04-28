package com.connorcode.universaltick;

import com.connorcode.universaltick.commands.About;
import com.connorcode.universaltick.commands.TickGet;
import com.connorcode.universaltick.commands.TickSet;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static com.mojang.brigadier.arguments.FloatArgumentType.floatArg;

public class Commands {
    Command[] commands =
            new Command[]{new About(), new TickGet(), new TickSet()};

    private static int easyErr(CommandContext<ServerCommandSource> ctx,
                               String message) {
        try {
            ctx.getSource()
                    .getPlayer()
                    .sendMessage(Text.of(message), true);
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }

        return 1;
    }

    void initCommands() {
        CommandRegistrationCallback.EVENT.register(
                (dispatcher, dedicated) -> dispatcher.register(CommandManager.literal("tick")
                        .then(CommandManager.literal("set")
                                .then(CommandManager.argument("tick", floatArg(0.1F, 500F)))
                                .executes(ctx -> commands[2].execute(ctx))
                                .executes(ctx -> easyErr(ctx, "No tick rate provided")))
                        .then(CommandManager.literal("get")
                                .executes(ctx -> commands[1].execute(ctx)))
                        .then(CommandManager.literal("about")
                                .executes(ctx -> commands[0].execute(ctx)))
                        .executes(ctx -> easyErr(ctx, "No tick subcommand provided"))));
    }
}
