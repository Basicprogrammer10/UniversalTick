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
import org.jetbrains.annotations.NotNull;

import static com.mojang.brigadier.arguments.StringArgumentType.greedyString;

public class Commands {
    // Create Commands
    Command aboutCommand = new About();
    Command tickGetCommand = new TickGet();
    Command tickSetCommand = new TickSet();

    // Easy way to exit after sending an error message to the players
    public static int easyErr(@NotNull CommandContext<ServerCommandSource> ctx,
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

    // Add the commands to the server
    void initCommands() {
        CommandRegistrationCallback.EVENT.register(
                (dispatcher, dedicated) -> dispatcher.register(CommandManager.literal("tick")
                        .then(CommandManager.literal("set")
                                .then(CommandManager.argument("tick", greedyString())
                                        .executes(ctx -> tickSetCommand.execute(ctx)))
                                .executes(ctx -> easyErr(ctx, "No tick rate provided")))
                        .then(CommandManager.literal("get")
                                .executes(ctx -> tickGetCommand.execute(ctx)))
                        .then(CommandManager.literal("about")
                                .executes(ctx -> aboutCommand.execute(ctx)))
                        .executes(ctx -> easyErr(ctx, "No tick subcommand provided"))));
    }
}
