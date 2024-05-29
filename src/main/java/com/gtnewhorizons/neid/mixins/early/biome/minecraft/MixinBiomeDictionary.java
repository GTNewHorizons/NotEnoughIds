package com.gtnewhorizons.neid.mixins.early.biome.minecraft;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.gtnewhorizons.neid.PlaceholderBiome;

@Mixin(BiomeDictionary.class)
public abstract class MixinBiomeDictionary {

    @Inject(method = "registerAllBiomesAndGenerateEvents", at = @At("HEAD"), require = 1, remap = false)
    private static void cleanupBiomeArray(CallbackInfo ci) {
        BiomeGenBase[] biomes = BiomeGenBase.getBiomeGenArray();
        for (int i = 0; i < biomes.length; i++) {
            if (biomes[i] instanceof PlaceholderBiome) {
                biomes[i] = null;
            }
        }
    }
}
