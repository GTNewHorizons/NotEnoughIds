package com.gtnewhorizons.neid.mixins.late.biome.rwg;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.gtnewhorizons.neid.mixins.ChunkProviderPatchHelper;

@Pseudo
@Mixin(targets = "rwg.world.ChunkGeneratorRealistic", remap = false)
public class MixinChunkGeneratorRealistic {

    @Shadow()
    private BiomeGenBase[] baseBiomesList;

    @Redirect(
            method = "func_73154_d",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/Chunk;func_76605_m()[B"),
            require = 1)
    private byte[] neid$overrideGetBiomeArray(Chunk chunk) {
        return ChunkProviderPatchHelper.getBiomeArrayReplacer(chunk, baseBiomesList);
    }
}
