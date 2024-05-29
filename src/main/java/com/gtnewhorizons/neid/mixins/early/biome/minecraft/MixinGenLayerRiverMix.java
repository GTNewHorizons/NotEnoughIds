package com.gtnewhorizons.neid.mixins.early.biome.minecraft;

import net.minecraft.world.gen.layer.GenLayerRiverMix;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(GenLayerRiverMix.class)
public class MixinGenLayerRiverMix {

    @ModifyConstant(method = "getInts", constant = @Constant(intValue = 255), require = 1)
    private int extendTheID(int constant) {
        return 65535;
    }
}
