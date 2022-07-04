package com.connorcode.universaltick.mixin;

import com.connorcode.universaltick.client.UniversalTickClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.GameRenderer;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// Modified from https://github.com/Wartori54/TRC-No-Lag
@Mixin(MinecraftClient.class)
public abstract class ClientMixin {
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
        if (currentScreen != null) return;
//        for (int i = 0; i < Math.min(10, UniversalTickClient.stableTicksToDo); i++) {
        this.handleInputEvents();
        this.gameRenderer.updateTargetedEntity(1.0F);
//        }
    }

    @Inject(method = "render", at = @At("HEAD"))
    private void render(boolean tick, CallbackInfo ci) {
        long now = System.currentTimeMillis();
        if (UniversalTickClient.lastFrameTimestamp == -1) {
            UniversalTickClient.lastFrameTimestamp = now;
            return;
        }

        UniversalTickClient.avgFrameTime.add(now - UniversalTickClient.lastFrameTimestamp);
        UniversalTickClient.lastFrameTimestamp = now;
        while (UniversalTickClient.avgFrameTime.size() > 500) UniversalTickClient.avgFrameTime.remove(0);
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;" +
            "handleInputEvents()V"))
    private void handleInputEventsRedirect(MinecraftClient minecraftClient) {
    }

//    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;" +
//            "handleInputEvents()V", shift = At.Shift.AFTER))
//    void tickModifyAttackCooldown(CallbackInfo ci) {
//        long now = System.currentTimeMillis();
//        if (attackCooldown > 0 && now - UniversalTickClient.lastCooldownUpdateTimestamp >= UniversalTickClient.clientTickSpeed * UniversalTickClient.getAvgFrameTime()) {
//            UniversalTickClient.lastCooldownUpdateTimestamp = now;
//            attackCooldown--;
//        }
//    }
//
//    @Redirect(method = "tick", at = @At(value = "FIELD", target = "Lnet/minecraft/client/MinecraftClient;" +
//            "attackCooldown:I", opcode = Opcodes.GETFIELD))
//    int tickAttackCooldown(MinecraftClient instance) {
//        return 0;
//    }

//    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/RenderTickCounter;"
//            + "beginRenderTick(J)I"))
//    private int storeTicksToDo(RenderTickCounter renderTickCounter, long timeMillis) {
//        int tick = renderTickCounter.beginRenderTick(timeMillis);
////        UniversalTickClient.ticksToGetDone += tick;
//        return tick;
////        UniversalTickClient.ticksToDo = renderTickCounter.beginRenderTick(timeMillis);
////        UniversalTickClient.ticksToGetDone += UniversalTickClient.ticksToDo;
////        return UniversalTickClient.ticksToDo;
//    }

    @Inject(method = "disconnect(Lnet/minecraft/client/gui/screen/Screen;)V", at = @At("TAIL"))
    private void onDisconnect(Screen screen, CallbackInfo ci) {
        UniversalTickClient.setClientTickSpeed(50);
    }
}
