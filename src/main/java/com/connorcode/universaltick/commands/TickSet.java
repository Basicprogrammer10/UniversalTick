package com.connorcode.universaltick.commands;

import com.connorcode.universaltick.Command;
import com.connorcode.universaltick.Commands;
import com.connorcode.universaltick.UniversalTick;
import com.connorcode.universaltick.UniversalTick.RateChange;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static com.mojang.brigadier.arguments.StringArgumentType.getString;

public class TickSet implements Command {
    @Override
    public int execute(@NotNull CommandContext<ServerCommandSource> ctx) {
        // Get and parse the tps
        String rawTps = getString(ctx, "tick");
        Optional<Float> parseResult = parseTps(rawTps);
        Optional<RateChange> changeType = parseType(ctx);

        // If parsing fails send an error
        if (parseResult.isEmpty())
            return Commands.easyErr(ctx, "Invalid tick speed");

        if (changeType.isEmpty())
            return Commands.easyErr(ctx, "Invalid change type");

        // Tick Rate Limits (0 - 500)
        if (parseResult.get() > 500)
            return Commands.easyErr(ctx, "Lets keep the tick speed to 500 or under");

        if (parseResult.get() <= 0)
            return Commands.easyErr(ctx, "Are you trying to freeze the event loop?");

        // Set the server tps and notify all clients
        UniversalTick.setTps(parseResult.get(), changeType.get());

        // Reset the TPS history so it's accurate more quickly
        UniversalTick.tickInfo.tickHistory.clear();

        // Send message with new tick speed
        try {
            ctx.getSource()
                    .getPlayer()
                    .sendMessage(Text.of(String.format("Set%s Tick Speed To %.1f", typeString(changeType.get()),
                            parseResult.get())), true);
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }

        return 1;
    }

    // Little function to add what type is being changed to the message
    private String typeString(RateChange type) {
        return switch (type) {
            case Client -> " Client";
            case Server -> " Server";
            case Universal -> "";
        };
    }

    // parse the type,,,
    // this really didn't need a comment
    private Optional<RateChange> parseType(CommandContext<ServerCommandSource> ctx) {
        String type;
        try {
            type = getString(ctx, "type");
        } catch (IllegalArgumentException e) {
            return Optional.of(RateChange.Universal);
        }

        return switch (type) {
            case "server" -> Optional.of(RateChange.Server);
            case "clients" -> Optional.of(RateChange.Client);
            case "universal" -> Optional.of(RateChange.Universal);
            default -> Optional.empty();
        };
    }

    // Parse TPS from string
    private Optional<Float> parseTps(String raw) {
        // Percent based TPS
        // EX: 100%, 54.2%
        if (raw.endsWith("p")) {
            float percent;
            try {
                percent = Float.parseFloat(raw.replace("p", ""));
            } catch (NumberFormatException e) {
                return Optional.empty();
            }
            float tps = (percent / 100) * 20;
            return Optional.of(tps);
        }

        // Tick based TPS
        // EX: 6.5, 20
        float tps;
        try {
            tps = Float.parseFloat(raw);
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
        return Optional.of(tps);
    }
}
