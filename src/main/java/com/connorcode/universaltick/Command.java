package com.connorcode.universaltick;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;

public interface Command {
    int execute(CommandContext<ServerCommandSource> ctx);
}
