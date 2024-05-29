package com.gtnewhorizons.neid.mixins.late.biome.extrautils;

import java.util.Arrays;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.gtnewhorizons.neid.mixins.ChunkProviderPatchHelper;
import com.gtnewhorizons.neid.mixins.interfaces.IChunkMixin;

@Pseudo
@Mixin(targets = "com.rwtema.extrautils.worldgen.endoftime.ChunkProviderEndOfTime", remap = false)
public class MixinChunkProviderEndOfTime {

    // TODO: is this stuff thread safe? Almost certainly not, not a problem until someone goes
    // and multi-threads world gen
    @Unique
    private Chunk chunk;

    @Unique
    private int id;

    @Redirect(
            method = "func_73154_d",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/Chunk;func_76605_m()[B"),
            require = 1)
    private byte[] neid$overrideGetBiomeArray(Chunk chunk) {
        this.chunk = chunk;
        return ChunkProviderPatchHelper.ZERO_LENGTH_BYTE_ARRAY;
    }

    @Redirect(
            method = "func_73154_d",
            at = @At(value = "FIELD", target = "Lnet/minecraft/world/biome/BiomeGenBase;field_76756_M:I"),
            require = 1)
    private int neid$captureBiomeID(BiomeGenBase biomeGenBase) {
        this.id = biomeGenBase.biomeID;
        return this.id;
    }

    @Redirect(method = "func_73154_d", at = @At(value = "INVOKE", target = "Ljava/util/Arrays;fill([BB)V"), require = 1)
    private void neid$overrideArrayFill(byte[] bytes, byte b) {
        try {
            Arrays.fill(((IChunkMixin) this.chunk).getBiome16BArray(), (short) id);
        } finally {
            chunk = null;
            id = -1;
        }
    }

}
