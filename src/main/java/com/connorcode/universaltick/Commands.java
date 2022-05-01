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

import static com.mojang.brigadier.arguments.BoolArgumentType.bool;
import static com.mojang.brigadier.arguments.BoolArgumentType.getBool;
import static com.mojang.brigadier.arguments.StringArgumentType.string;
import static net.minecraft.command.CommandSource.suggestMatching;

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
                                .requires((x) -> x.hasPermissionLevel(4))
                                .then(CommandManager.argument("tick", string())
                                        .suggests((c, b) -> suggestMatching(new String[]{"20.0", "100p"}, b))
                                        .executes(tickSetCommand::execute)
                                        .then(CommandManager.argument("type", string())
                                                .suggests((c, b) -> suggestMatching(
                                                        new String[]{"server", "clients", "universal"}, b))
                                                .executes(tickSetCommand::execute)))
                                .executes(ctx -> easyErr(ctx, "No tick rate provided")))
                        .then(CommandManager.literal("get")
                                .executes(tickGetCommand::execute))
                        .then(CommandManager.literal("about")
                                .executes(aboutCommand::execute))
                        .executes(ctx -> easyErr(ctx, "No tick subcommand provided"))
                        .then(CommandManager.literal("config")
                                .requires((x) -> x.hasPermissionLevel(4))
                                .then(CommandManager.literal("clientMouse")
                                        .then(CommandManager.argument("value", bool())
                                                .executes(ctx -> {
                                                    Settings.clientMouse = getBool(ctx, "value");
                                                    Settings.broadcastSettings();
                                                    return 1;
                                                })))
                                .then(CommandManager.literal("clientSound")
                                        .then(CommandManager.argument("value", bool())
                                                .executes(ctx -> {
                                                    Settings.clientSound = getBool(ctx, "value");
                                                    Settings.broadcastSettings();
                                                    return 1;
                                                }))))));
    }
}
