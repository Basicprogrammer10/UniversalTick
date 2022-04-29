package com.connorcode.universaltick;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class UniversalTick implements ModInitializer {
    public static final Identifier SET_TICK_SPEED_PACKET = new Identifier("universialtick", "tickspeed");
    public static MinecraftServer server;
    public static long targetMSPT = 50;

    // Get the target tick speed as TPS
    public static float getTps() {
        // TODO: Support large TPSs
        return 1F / (float) targetMSPT * 1000F;
    }

    // Set target tick speed and update connected clients
    public static void setTps(float tps, boolean updateClients) {
        // Set Target MSPT
        targetMSPT = (long) ((1.0 / tps) * 1000);
        if (!updateClients) return;

        // Update tick speed for all clients
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeLong(targetMSPT);
        for (ServerPlayerEntity p : server.getPlayerManager()
                .getPlayerList())
            ServerPlayNetworking.send(p, SET_TICK_SPEED_PACKET, buf);
    }

    @Override
    public void onInitialize() {
        // Get and save reference to the server
        ServerLifecycleEvents.SERVER_STARTED.register(minecraftServer -> server = minecraftServer);

        // Init server side command system
        new Commands().initCommands();
    }
}
