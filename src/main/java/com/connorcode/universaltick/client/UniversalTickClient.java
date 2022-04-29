package com.connorcode.universaltick.client;

import com.connorcode.universaltick.UniversalTick;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class UniversalTickClient implements ClientModInitializer {
    public static float targetTps = 20;

    @Override
    public void onInitializeClient() {
        // Create Tick Speed Packet Listener
        ClientPlayNetworking.registerGlobalReceiver(UniversalTick.SET_TICK_SPEED_PACKET, (client, handler, buf, responseSender) -> {
            // Read MSPT and calculate target TPS
            long mspt = buf.readLong();
            targetTps = 1F / (float) mspt * 1000F;

            client.execute(() -> {
                // Show the new target tps on the window title
                client.getWindow().setTitle(String.format("Minecraft %s | Target TPS: %f", client.getGameVersion(), targetTps));

                // DEBUG
                client.inGameHud.setOverlayMessage(Text.of(String.format("CLIENT: %f", targetTps)), false);
            });
        });
    }
}
