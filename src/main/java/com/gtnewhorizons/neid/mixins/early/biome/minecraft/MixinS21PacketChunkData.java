package com.gtnewhorizons.neid.mixins.early.biome.minecraft;

import net.minecraft.network.play.server.S21PacketChunkData;
import net.minecraft.world.chunk.Chunk;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.gtnewhorizons.neid.mixins.interfaces.IChunkMixin;

@Mixin(S21PacketChunkData.class)
public class MixinS21PacketChunkData {

    @Redirect(
            method = "func_149269_a",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/Chunk;getBiomeArray()[B"),
            require = 1)
    private static byte[] neid$overrideGetBiomeArray(Chunk chunk) {
        IChunkMixin chunkMixin = (IChunkMixin) chunk;
        return chunkMixin.getBiomeData();
    }
}
