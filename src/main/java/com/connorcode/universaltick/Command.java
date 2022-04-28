package com.connorcode.universaltick;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;

public abstract class Command {
    public abstract int execute(CommandContext<ServerCommandSource> ctx);
}
