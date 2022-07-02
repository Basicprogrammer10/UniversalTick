package com.connorcode.universaltick.mixin;

import com.connorcode.universaltick.client.UniversalTickClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.class)
public class AbstractBlockMixin {
    @Inject(method = "calcBlockBreakingDelta", at = @At("RETURN"), cancellable = true)
    private void calcBlockBreakingDelta(BlockState state, PlayerEntity player, BlockView world, BlockPos pos,
                                        CallbackInfoReturnable<Float> cir) {
        if (FabricLoader.getInstance().getEnvironmentType() != EnvType.CLIENT) return;
        cir.setReturnValue(cir.getReturnValue() / (UniversalTickClient.clientTickSpeed / 50.0F));
    }
}
