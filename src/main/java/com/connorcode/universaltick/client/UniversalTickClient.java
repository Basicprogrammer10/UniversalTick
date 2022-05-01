package com.connorcode.universaltick.client;

import com.connorcode.universaltick.UniversalTick;
import com.connorcode.universaltick.mixin.ClientRenderTickCounter;
import com.connorcode.universaltick.mixin.ClientTickEvent;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class UniversalTickClient implements ClientModInitializer {
    public static long clientTickSpeed = 50;

    @Override
    public void onInitializeClient() {
        // Create Tick Speed Packet Listener
        ClientPlayNetworking.registerGlobalReceiver(UniversalTick.SET_TICK_SPEED_PACKET,
                (client, handler, buf, responseSender) -> {
                    // Read MSPT and calculate target TPS
                    long mspt = buf.readLong();
                    clientTickSpeed = mspt;

                    client.execute(() -> setClientTickSpeed(mspt));
                    client.getToastManager().add(new SystemToast(SystemToast.Type.TUTORIAL_HINT, Text.of("UniversalTick"), Text.of("Got new tick speed")));
                });
    }

    public static void setClientTickSpeed(float mspt) {
        ((ClientRenderTickCounter) ((ClientTickEvent) MinecraftClient.getInstance()).renderTickCounter()).tickTime(
                mspt);
    }
}
