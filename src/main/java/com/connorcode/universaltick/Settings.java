package com.connorcode.universaltick;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

import static com.connorcode.universaltick.UniversalTick.SETTING_SYNC_PACKET;

public class Settings {
    public final String[] settings = new String[]{
            "clientMouse", "clientSound"
    };

    public boolean clientMouse = true;
    public boolean clientSound = true;

    public NbtCompound asNbt() {
        NbtCompound data = new NbtCompound();
        data.putBoolean("clientMouse", clientMouse);
        data.putBoolean("clientSound", clientSound);
        return data;
    }

    public void broadcastSettings() {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeNbt(asNbt());
        for (ServerPlayerEntity p : UniversalTick.server.getPlayerManager()
                .getPlayerList())
            ServerPlayNetworking.send(p, SETTING_SYNC_PACKET, buf);
    }
}