package com.connorcode.universaltick.commands;

import com.connorcode.universaltick.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

public class About implements Command {
    @Override
    public int execute(@NotNull CommandContext<ServerCommandSource> ctx) {
        try {
            ctx.getSource()
                    .getPlayer()
                    .sendMessage(Text.of("UniversalTick Mod V1.0 by Sigma#8214"), true);
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }

        return 1;
    }
}
