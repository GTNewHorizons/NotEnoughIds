package com.gtnewhorizons.neid.mixins.late.biome.gt5u;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.gtnewhorizons.neid.mixins.ChunkProviderPatchHelper;

@Pseudo
@Mixin(targets = "com.github.bartimaeusnek.crossmod.galacticraft.planets.ross128b.ChunkProviderRoss128b", remap = false)
public class MixinChunkProviderRoss128b {

    @Shadow
    private BiomeGenBase[] biomesForGeneration;

    @Redirect(
            method = "func_73154_d",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/Chunk;func_76605_m()[B"),
            require = 1)
    private byte[] neid$overrideGetBiomeArray(Chunk chunk) {
        return ChunkProviderPatchHelper.getBiomeArrayReplacer(chunk, biomesForGeneration);
    }

}
