package com.connorcode.universaltick.mixin;

import com.connorcode.universaltick.UniversalTick;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.connorcode.universaltick.UniversalTick.SET_TICK_SPEED_PACKET;

@Mixin(PlayerManager.class)
public class PlayerJoin {
    @Inject(at = @At(value = "TAIL"), method = "onPlayerConnect")
    void onPlayerConnect(ClientConnection connection, ServerPlayerEntity player, CallbackInfo info) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeLong(UniversalTick.clientTargetMSPT);
        ServerPlayNetworking.send(player, SET_TICK_SPEED_PACKET, buf);
    }
}
