package com.connorcode.universaltick.mixin;

import com.connorcode.universaltick.client.UniversalTickClient;
import net.minecraft.client.util.SmoothUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(SmoothUtil.class)
public class SmoothUtilMixin {
    @ModifyVariable(method = "smooth", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private double smoother(double x) {
        return x * 50f / UniversalTickClient.clientTickSpeed;
    }
}
