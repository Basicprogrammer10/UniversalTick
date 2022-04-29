package com.connorcode.universaltick.mixin;

import com.connorcode.universaltick.TickInfo;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

// Try to get TPS
@Mixin(MinecraftServer.class)
public class TickEvent {
    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        TickInfo.processTick();
    }
}
