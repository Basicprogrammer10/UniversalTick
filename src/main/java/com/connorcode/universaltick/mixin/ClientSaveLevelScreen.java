package com.connorcode.universaltick.mixin;

import com.connorcode.universaltick.client.UniversalTickClient;
import net.minecraft.client.gui.screen.SaveLevelScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SaveLevelScreen.class)
public class ClientSaveLevelScreen {
    // Reset client tick speed when going to the level load screen
    @Inject(method = {"<init>"}, at = {@At("TAIL")})
    void resetTickTime(CallbackInfo info) {
        UniversalTickClient.setClientTickSpeed(50);
        UniversalTickClient.sentServerToast = false;
    }
}
