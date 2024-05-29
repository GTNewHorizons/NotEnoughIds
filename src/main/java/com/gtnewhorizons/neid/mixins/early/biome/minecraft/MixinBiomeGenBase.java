package com.gtnewhorizons.neid.mixins.early.biome.minecraft;

import net.minecraft.world.biome.BiomeGenBase;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.gtnewhorizons.neid.PlaceholderBiome;

@Mixin(BiomeGenBase.class)
public class MixinBiomeGenBase {

    @Shadow
    @Final
    private static BiomeGenBase[] biomeList;

    @Shadow
    @Final
    public int biomeID;

    @ModifyConstant(method = "<clinit>", constant = @Constant(intValue = 256), require = 1)
    private static int neid$overrideBiomeIDCount(int oldValue) {
        return 65536;
    }

    @Inject(method = "<init>(IZ)V", at = @At(value = "RETURN"), require = 1)
    private void generatePlaceholders(int id, boolean register, CallbackInfo ci) {
        if (((Object) this) instanceof PlaceholderBiome || !register) {
            return;
        }
        if (biomeID >= 128 && biomeList[biomeID - 128] == null) {
            new PlaceholderBiome(biomeID - 128, (BiomeGenBase) (Object) this);
        }
        if (biomeID <= 65536 - 129 && biomeList[biomeID + 128] == null) {
            new PlaceholderBiome(biomeID + 128, (BiomeGenBase) (Object) this);
        }
    }

}
