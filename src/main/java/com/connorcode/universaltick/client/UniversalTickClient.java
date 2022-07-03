package com.connorcode.universaltick.client;

import com.connorcode.universaltick.Settings;
import com.connorcode.universaltick.UniversalTick;
import com.connorcode.universaltick.Util;
import com.connorcode.universaltick.mixin.ClientRenderTickCounter;
import com.connorcode.universaltick.mixin.ClientTickEvent;
import com.connorcode.universaltick.mixin.RenderTickCounterMixin;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;

import java.io.IOException;

@Environment(EnvType.CLIENT)
public class UniversalTickClient implements ClientModInitializer {
    public static Settings settings = new Settings();
    public static long clientTickSpeed = 50;
    public static boolean sentServerToast = false;

//    public static int stableTicksToDo = 0;
//    public static int ticksToDo = 0;
//    public static int ticksToGetDone = 0;
    public static long lastBlockBreak = 0;

    public static void setClientTickSpeed(float mspt) {
        ((ClientRenderTickCounter) ((ClientTickEvent) MinecraftClient.getInstance()).renderTickCounter()).tickTime(
                mspt);
//        ((RenderTickCounterMixin) renderTickCounter).setTickTime(mspt);
    }

    @Override
    public void onInitializeClient() {
        // Create Tick Speed Packet Listener
        ClientPlayNetworking.registerGlobalReceiver(UniversalTick.SET_TICK_SPEED_PACKET,
                (client, handler, buf, responseSender) -> {
                    // Read MSPT and calculate target TPS
                    long mspt = buf.readLong();
                    clientTickSpeed = mspt;

                    // Go onto the render thread, set the tick speed and show a toast :p
                    client.execute(() -> {
                        setClientTickSpeed(mspt);

                        if (sentServerToast) return;
                        sentServerToast = true;
                        client.getToastManager()
                                .add(new SystemToast(SystemToast.Type.TUTORIAL_HINT, Text.of("Universal-Tick"),
                                        Text.of("Server has UniversalTick")));
                    });
                });

        // Create Setting Sync Packet Listener
        ClientPlayNetworking.registerGlobalReceiver(UniversalTick.SETTING_SYNC_PACKET,
                (client, handler, buf, responseSender) -> {
                    NbtCompound data = buf.readNbt();
                    assert data != null;

                    // Set local settings from packet
                    settings.clientMouse = data.getBoolean("clientMouse");
                    settings.clientSound = data.getBoolean("clientSound");
                });

        try {
            Util.checkVersion();
        } catch (IOException ignored) {
        }
    }
}
