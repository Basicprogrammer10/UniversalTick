package com.connorcode.universaltick.commands;

import com.connorcode.universaltick.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class TickSet implements Command {
    @Override
    public int execute(CommandContext<ServerCommandSource> ctx) {
        try {
            ctx.getSource().getPlayer().sendMessage(Text.of("Set Tick Speed To XX"), true);
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }

        return 1;
    }
}
