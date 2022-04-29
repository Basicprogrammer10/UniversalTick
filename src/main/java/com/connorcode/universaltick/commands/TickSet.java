package com.connorcode.universaltick.commands;

import com.connorcode.universaltick.Command;
import com.connorcode.universaltick.Commands;
import com.connorcode.universaltick.UniversalTick;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static com.mojang.brigadier.arguments.StringArgumentType.getString;

public class TickSet implements Command {
    @Override
    public int execute(@NotNull CommandContext<ServerCommandSource> ctx) {
        // Get and parse the tps
        String rawTps = getString(ctx, "tick");
        Optional<Float> parseResult = parseTps(rawTps);

        // If parsing fails send an error
        if (parseResult.isEmpty())
            return Commands.easyErr(ctx, "Invalid Tick Speed");

        // Set the server tps and notify all clients
        UniversalTick.setTps(parseResult.get(), true);

        // Send message with new tick speed
        // Also show some *fancy* particles
        try {
            ctx.getSource().getPlayer().sendMessage(Text.of(String.format("Set Tick Speed To %f", parseResult.get())), true);

            Vec3d pos = ctx.getSource().getPlayer().getPos();
            ctx.getSource().getWorld().spawnParticles(ParticleTypes.EXPLOSION, pos.x, pos.y, pos.z, 1, 0D, 0D, 0D, 1.0);
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }

        return 1;
    }

    // Parse TPS from string
    private Optional<Float> parseTps(String raw) {
        // Percent based TPS
        // EX: 100%, 54.2%
        if (raw.endsWith("%")) {
            float percent;
            try {
                percent = Float.parseFloat(raw.replace("%", ""));
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
