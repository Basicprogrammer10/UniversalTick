package com.connorcode.universaltick.mixin;

import com.connorcode.universaltick.client.UniversalTickClient;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundSystem;
import net.minecraft.util.math.MathHelper;
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
        float normalPitch = MathHelper.clamp(instance.getPitch(), 0.5F, 2.0F);
        returnable.setReturnValue(50.0F / UniversalTickClient.clientTickSpeed * normalPitch);
        returnable.cancel();
    }
}
