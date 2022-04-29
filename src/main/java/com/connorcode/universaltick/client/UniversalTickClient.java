package com.connorcode.universaltick.client;

import com.connorcode.universaltick.UniversalTick;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class UniversalTickClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(UniversalTick.SET_TICK_SPEED_PACKET, (client, handler, buf, responseSender) -> {
            client.inGameHud.setOverlayMessage(Text.of(String.format("CLIENT: %f", 1.0)), false);
        });
    }
}
