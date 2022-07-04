package com.connorcode.universaltick.mixin;

import com.connorcode.universaltick.client.UniversalTickClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {
    int x;
    @Shadow
    private int blockBreakingCooldown;

    @Shadow
    private float blockBreakingSoundCooldown;

    @Inject(method = "updateBlockBreakingProgress", at = @At(value = "INVOKE", target = "Lnet/minecraft/client" +
            "/network/ClientPlayerInteractionManager;syncSelectedSlot()V", shift = At.Shift.AFTER), cancellable = true)
    private void updateBlockBreakingCooldown(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        if (this.blockBreakingCooldown <= 0) return;
        cir.setReturnValue(true);

        if (Util.getEpochTimeMs() - UniversalTickClient.lastCooldownUpdateTimestamp >= 50) {
            this.blockBreakingCooldown--;
            System.out.printf("DECREMENT: %d\n", this.blockBreakingCooldown);
            UniversalTickClient.lastCooldownUpdateTimestamp = Util.getEpochTimeMs();
        }
    }

    @Redirect(method = "updateBlockBreakingProgress", at = @At(value = "FIELD", target = "Lnet/minecraft/client" +
            "/network/ClientPlayerInteractionManager;blockBreakingCooldown:I", opcode = Opcodes.GETFIELD, ordinal = 0))
    private int removeBlockBreakingCooldownUpdate(ClientPlayerInteractionManager clientPlayerInteractionManager) {
        return 0;
    }

//    @Inject(method = "attackBlock", at = @At("RETURN"))
//    void attackBlock(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
//        if (cir.getReturnValueZ()) {
//            UniversalTickClient.lastBlockBreak = System.currentTimeMillis();
//            System.out.println("RESETTING TIME");
//        }
//    }
}
