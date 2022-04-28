package com.connorcode.universaltick;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static com.mojang.brigadier.arguments.FloatArgumentType.floatArg;
import static com.mojang.brigadier.arguments.FloatArgumentType.getFloat;

public class Command {
    static void initCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            dispatcher.register(CommandManager.literal("tick").then(CommandManager.literal("set").then(CommandManager.argument("tick", floatArg(0.1F, 500F))).executes(ctx -> {
                float rate = getFloat(ctx, "tick");
                System.out.printf("Setting Tick Speed To: %f", rate);
                return 1;
            }).executes(ctx -> easyErr(ctx, "No tick rate provided"))).then(CommandManager.literal("get").executes(ctx -> {
                System.out.println("Getting Tick Rate");
                return 1;
            })).executes(ctx -> easyErr(ctx, "No tick subcommand provided")));
        });
    }

    private static int easyErr(CommandContext<ServerCommandSource> ctx, String message) {
        try {
            ctx.getSource().getPlayer().sendMessage(Text.of(message), true);
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }
        return 1;
    }
}
