package com.connorcode.universaltick.mixin;

import com.connorcode.universaltick.client.UniversalTickClient;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RenderPhase.class)
public class ClientRenderItem {
    @Redirect(method = "setupGlintTexturing", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Util;getMeasuringTimeMs()J"))
    private static long getEnchantmentTime() {
        return (long) (Util.getMeasuringTimeMs() / (UniversalTickClient.clientTickSpeed / 50.0));
    }
}
