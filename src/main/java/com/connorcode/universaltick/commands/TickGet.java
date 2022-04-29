package com.connorcode.universaltick.commands;

import com.connorcode.universaltick.Command;
import com.connorcode.universaltick.UniversalTick;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class TickGet extends Command {
    @Override
    public int execute(CommandContext<ServerCommandSource> ctx) {
        try {
            ctx.getSource()
                    .getPlayer()
                    .sendMessage(Text.of(String.format("Target Tick Speed: %f", UniversalTick.getTps())), true);
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }

        return 1;
    }
}
