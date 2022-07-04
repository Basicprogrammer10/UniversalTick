package com.connorcode.universaltick.mixin;

import com.connorcode.universaltick.client.UniversalTickClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.Util;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class ClientMixin {
    private static long lastAttackCooldownUpdateTimestamp;

    @Shadow
    @Nullable
    public Screen currentScreen;
    @Shadow
    @Final
    public GameRenderer gameRenderer;
    @Shadow
    protected int attackCooldown;

    @Shadow
    protected abstract void handleInputEvents();

    @Shadow
    public abstract void tick();

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Mouse;updateMouse()V"))
    private void checkInputs(boolean tick, CallbackInfo ci) {
        if (currentScreen != null || Util.getEpochTimeMs() - UniversalTickClient.lastBlockHitTimestamp < UniversalTickClient.clientTickSpeed)
            return;
        this.handleInputEvents();
        this.gameRenderer.updateTargetedEntity(1.0F);
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;" +
            "handleInputEvents()V"))
    private void handleInputEventsRedirect(MinecraftClient minecraftClient) {
    }

    @Inject(method = "disconnect(Lnet/minecraft/client/gui/screen/Screen;)V", at = @At("TAIL"))
    private void onDisconnect(Screen screen, CallbackInfo ci) {
        UniversalTickClient.setClientTickSpeed(50);
    }
}
