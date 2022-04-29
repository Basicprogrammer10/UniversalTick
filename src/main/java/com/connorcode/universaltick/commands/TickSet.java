package com.connorcode.universaltick.commands;

import com.connorcode.universaltick.Command;
import com.connorcode.universaltick.UniversalTick;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static com.mojang.brigadier.arguments.FloatArgumentType.getFloat;

public class TickSet extends Command {
    @Override
    public int execute(CommandContext<ServerCommandSource> ctx) {
        float tps = getFloat(ctx, "tick");
        UniversalTick.setTps(tps);

        try {
            ctx.getSource().getPlayer().sendMessage(Text.of(String.format("Set Tick Speed To %f", tps)), true);
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }

        return 1;
    }
}
