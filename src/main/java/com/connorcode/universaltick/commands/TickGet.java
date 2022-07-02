package com.connorcode.universaltick.commands;

import com.connorcode.universaltick.Command;
import com.connorcode.universaltick.UniversalTick;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

public class TickGet implements Command {
    @Override
    public int execute(@NotNull CommandContext<ServerCommandSource> ctx) {
        try {
            ctx.getSource().getPlayer()
                    // [Server Target TPS, Client Target TPS] × Real Server TPS
                    .sendMessage(Text.of(String.format("[%.1f, %.1f] ⌂ %.1f", UniversalTick.getTps(),
                            UniversalTick.getClientTps(), UniversalTick.tickInfo.getTps())), true);
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }

        return 1;
    }
}
