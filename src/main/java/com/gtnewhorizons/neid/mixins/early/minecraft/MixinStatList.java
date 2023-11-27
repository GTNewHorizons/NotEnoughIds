package com.gtnewhorizons.neid.mixins.early.minecraft;

import net.minecraft.stats.StatList;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import com.gtnewhorizons.neid.Constants;

@Mixin(StatList.class)
public class MixinStatList {

    @ModifyConstant(method = "<clinit>", constant = @Constant(intValue = 4096), require = 1)
    private static int neid$increaseBlockSize(int original) {
        return Constants.MAX_BLOCK_ID;
    }
}
