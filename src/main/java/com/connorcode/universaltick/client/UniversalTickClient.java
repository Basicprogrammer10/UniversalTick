package com.connorcode.universaltick.client;

import com.connorcode.universaltick.UniversalTick;
import com.connorcode.universaltick.mixin.ClientRenderTickCounter;
import com.connorcode.universaltick.mixin.ClientTickEvent;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;

@Environment(EnvType.CLIENT)
public class UniversalTickClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Create Tick Speed Packet Listener
        ClientPlayNetworking.registerGlobalReceiver(UniversalTick.SET_TICK_SPEED_PACKET,
                (client, handler, buf, responseSender) -> {
                    // Read MSPT and calculate target TPS
                    long mspt = buf.readLong();

                    client.execute(() -> setClientTickSpeed(mspt));
                });
    }

    public static void setClientTickSpeed(float mspt) {
        ((ClientRenderTickCounter) ((ClientTickEvent) MinecraftClient.getInstance()).renderTickCounter()).tickTime(
                mspt);
    }
}
