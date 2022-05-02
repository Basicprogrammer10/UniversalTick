package com.connorcode.universaltick.mixin;

import com.connorcode.universaltick.client.UniversalTickClient;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Mouse.class)
public class ClientMouse {
    // Modify client mouse sensitivity from the client tick rate
    @ModifyVariable(method = "updateMouse", at = @At(value = "STORE"), ordinal = 2)
    private double modifyMouseSensitivity(double og) {
        if (!UniversalTickClient.settings.clientMouse) return og;
        return 50D / (double) UniversalTickClient.clientTickSpeed * og;
    }
}
