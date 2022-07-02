package com.connorcode.universaltick;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.io.IOException;

public class UniversalTick implements ModInitializer {
    public static final String VERSION = "1.1";
    public static final Identifier SET_TICK_SPEED_PACKET = new Identifier("universialtick", "tickspeed");
    public static final Identifier SETTING_SYNC_PACKET = new Identifier("universialtick", "settings");
    public static long targetMSPT = 50;
    public static long clientTargetMSPT = 50;

    public static TickInfo tickInfo = new TickInfo();
    public static Settings settings = new Settings();
    public static MinecraftServer server;

    // Get the target tick speed as TPS
    public static float getTps() {
        return 1F / (float) targetMSPT * 1000F;
    }

    public static float getClientTps() {
        return 1F / (float) clientTargetMSPT * 1000F;
    }

    // Set target tick speed and update connected clients
    public static void setTps(float tps, RateChange updateType) {
        // Set Target MSPT
        if (updateType == RateChange.Server || updateType == RateChange.Universal)
            targetMSPT = (long) (1.0 / tps * 1000);

        // Update tick speed for all clients
        if (updateType == RateChange.Client || updateType == RateChange.Universal) {
            clientTargetMSPT = (long) (1.0 / tps * 1000);
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeLong(clientTargetMSPT);
            for (ServerPlayerEntity p : server.getPlayerManager().getPlayerList())
                ServerPlayNetworking.send(p, SET_TICK_SPEED_PACKET, buf);
        }
    }

    @Override
    public void onInitialize() {
        // Get and save reference to the server
        ServerLifecycleEvents.SERVER_STARTED.register(minecraftServer -> server = minecraftServer);

        // Reset everything
        ServerLifecycleEvents.SERVER_STOPPED.register(minecraftServer -> {
            targetMSPT = 50;
            clientTargetMSPT = 50;
            tickInfo = new TickInfo();
            settings = new Settings();
        });

        // Init server side command system
        new Commands().initCommands();
        try {
            Util.checkVersion();
        } catch (IOException ignored) {
        }
    }

    // The different targets of the rate change
    public enum RateChange {
        Server, Client, Universal
    }
}
