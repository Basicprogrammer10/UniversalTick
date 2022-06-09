package com.connorcode.universaltick.mixin;

import com.connorcode.universaltick.client.UniversalTickClient;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SoundSystem.class)
public class ClientSoundSystem {
    // Modify all sound playback speeds to match the client tick speed
    @Inject(method = {"getAdjustedPitch"}, at = {@At("HEAD")}, cancellable = true)
    void modifyPitch(SoundInstance instance, CallbackInfoReturnable<Float> returnable) {
        if (!UniversalTickClient.settings.clientSound) return;
        returnable.setReturnValue(50.0F / UniversalTickClient.clientTickSpeed);
        returnable.cancel();
    }
}
