package com.gtnewhorizons.neid.mixins.early.biome.minecraft;

import net.minecraft.world.gen.layer.GenLayerVoronoiZoom;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(GenLayerVoronoiZoom.class)
public class MixinGenLayerVoronoiZoom {

    @ModifyConstant(method = "getInts", constant = @Constant(intValue = 255), require = 2)
    private int neid$overrideBiomeIDMask(int constant) {
        return 65535;
    }
}
