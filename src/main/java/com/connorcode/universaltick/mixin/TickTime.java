package com.connorcode.universaltick.mixin;

import com.connorcode.universaltick.UniversalTick;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(MinecraftServer.class)
public class TickTime {
    // Modify the server tick rate
    @ModifyConstant(method = {"runServer"}, constant = {@Constant(longValue = 50L)})
    long modifyTickTime(long tickTime) {
        return UniversalTick.targetMSPT;
    }
}