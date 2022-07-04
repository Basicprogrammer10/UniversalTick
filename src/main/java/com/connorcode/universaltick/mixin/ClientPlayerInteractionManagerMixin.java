package com.connorcode.universaltick.mixin;

import com.connorcode.universaltick.client.UniversalTickClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.GameMode;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {
    private static long lastCooldownUpdateTimestamp;

    @Shadow
    private int blockBreakingCooldown;

    @Shadow
    private GameMode gameMode;

    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(method = "updateBlockBreakingProgress", at = @At(value = "INVOKE", target = "Lnet/minecraft/client" +
            "/network/ClientPlayerInteractionManager;syncSelectedSlot()V", shift = At.Shift.AFTER), cancellable = true)
    private void updateBlockBreakingCooldown(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        if (this.blockBreakingCooldown <= 0) return;
        cir.setReturnValue(true);

        if (Util.getEpochTimeMs() - lastCooldownUpdateTimestamp >= UniversalTickClient.clientTickSpeed) {
            this.blockBreakingCooldown--;
            lastCooldownUpdateTimestamp = Util.getEpochTimeMs();
        }
    }

    @Inject(method = "updateBlockBreakingProgress", at = @At("HEAD"))
    private void lastBlockBreakTimestampUpdate(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        if (this.blockBreakingCooldown > 0 || gameMode.isCreative() || Objects.requireNonNull(client.world)
                .getBlockState(pos).isAir()) return;
        UniversalTickClient.lastBlockHitTimestamp = Util.getEpochTimeMs();
    }

    @Redirect(method = "updateBlockBreakingProgress", at = @At(value = "FIELD", target = "Lnet/minecraft/client" +
            "/network/ClientPlayerInteractionManager;blockBreakingCooldown:I", opcode = Opcodes.GETFIELD, ordinal = 0))
    private int removeBlockBreakingCooldownUpdate(ClientPlayerInteractionManager clientPlayerInteractionManager) {
        return 0;
    }
}
