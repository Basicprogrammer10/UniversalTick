package com.connorcode.universaltick.commands;

import com.connorcode.universaltick.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class About extends Command {
    @Override
    public int execute(CommandContext<ServerCommandSource> ctx) {
        try {
            ctx.getSource().getPlayer().sendMessage(Text.of("UniversalTick Mod V0.0 by Sigma#8214"), true);
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }

        return 1;
    }
}