package com.gtnewhorizons.neid.mixins.early.minecraft;

import java.util.Arrays;

import net.minecraft.block.Block;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.gtnewhorizons.neid.Constants;

@Mixin(Block.class)
public class MixinBlock {

    @Shadow(remap = false)
    private String[] harvestTool = new String[Constants.METADATA_COUNT];

    @Shadow(remap = false)
    private int[] harvestLevel = new int[Constants.METADATA_COUNT];

    @Inject(method = "<init>(Lnet/minecraft/block/material/Material;)V", at = @At("TAIL"), require = 1)
    private void neid$OverrideHarvestToolMetadataSize(CallbackInfo ci) {
        Arrays.fill(this.harvestLevel, -1);
    }

}
