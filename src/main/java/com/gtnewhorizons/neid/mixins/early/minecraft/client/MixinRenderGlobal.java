package com.gtnewhorizons.neid.mixins.early.minecraft.client;

import net.minecraft.client.renderer.RenderGlobal;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import com.gtnewhorizons.neid.Constants;

@Mixin(RenderGlobal.class)
public class MixinRenderGlobal {

    @ModifyConstant(method = "playAuxSFX", constant = @Constant(intValue = 4095), require = 1)
    private static int neid$RenderGlobalplayAuxSFXConstant1(int i) {
        return Constants.BLOCK_ID_MASK;
    }

    @ModifyConstant(method = "playAuxSFX", constant = @Constant(intValue = 12), require = 1)
    private static int neid$RenderGlobalplayAuxSFXConstant2(int i) {
        return Constants.BITS_PER_ID;
    }

}
