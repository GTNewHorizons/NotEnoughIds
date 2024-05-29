package com.gtnewhorizons.neid.mixins.early.biome.minecraft.client;

import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

import net.minecraft.world.chunk.Chunk;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.gtnewhorizons.neid.mixins.interfaces.IChunkMixin;

@Mixin(Chunk.class)
public abstract class MixinChunk {

    @Redirect(
            method = "fillChunk",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/lang/System;arraycopy(Ljava/lang/Object;ILjava/lang/Object;II)V",
                    ordinal = 5))
    private void neid$overrideBiomeArrayCopy(Object thebytes, int offset, Object oldBrokenBytes, int dstPos,
            int length) {
        IChunkMixin chunkMixin = (IChunkMixin) this;
        byte[] theActualBytes = (byte[]) thebytes;
        ShortBuffer.wrap(chunkMixin.getBiome16BArray())
                .put(ByteBuffer.wrap(theActualBytes, offset, 256).asShortBuffer());
    }

    @Redirect(
            method = "fillChunk",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/chunk/Chunk;blockBiomeArray:[B",
                    args = "array=length"),
            require = 1)
    private int neid$canelBiomeArrayLength(byte[] arr) {
        return 0;
    }
}
