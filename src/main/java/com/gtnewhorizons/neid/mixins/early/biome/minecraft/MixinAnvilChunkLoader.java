package com.gtnewhorizons.neid.mixins.early.biome.minecraft;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.gtnewhorizons.neid.mixins.ChunkProviderPatchHelper;
import com.gtnewhorizons.neid.mixins.interfaces.IChunkMixin;
import com.llamalad7.mixinextras.sugar.Local;

@Mixin(AnvilChunkLoader.class)
public class MixinAnvilChunkLoader {

    @Redirect(
            method = "writeChunkToNBT",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/nbt/NBTTagCompound;setByteArray(Ljava/lang/String;[B)V",
                    ordinal = 6),
            require = 1)
    private void neid$overrideWriteChunkBiomeArray(NBTTagCompound tag, String s, byte[] oldbrokenbytes,
            @Local(ordinal = 0) Chunk chunk) {
        IChunkMixin chunkMixin = (IChunkMixin) chunk;
        tag.setByteArray("Biomes16", chunkMixin.getBiomeData());
    }

    @Redirect(
            method = "writeChunkToNBT",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/Chunk;getBiomeArray()[B", ordinal = 0),
            require = 1)
    private byte[] neid$cancelGetBiomeArray(Chunk chunk) {
        return ChunkProviderPatchHelper.ZERO_LENGTH_BYTE_ARRAY;
    }

    @Inject(
            method = "readChunkFromNBT",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/chunk/Chunk;setStorageArrays([Lnet/minecraft/world/chunk/storage/ExtendedBlockStorage;)V",
                    ordinal = 0,
                    shift = At.Shift.AFTER),
            require = 1)
    private void neid$overrideReadChunkBiomeArray(CallbackInfoReturnable<Boolean> ci, @Local(ordinal = 0) Chunk chunk,
            @Local(ordinal = 0) NBTTagCompound tag) {
        IChunkMixin chunkMixin = (IChunkMixin) chunk;
        if (tag.hasKey("Biomes16")) {
            chunkMixin.setBiomeData(tag.getByteArray("Biomes16"), 0);
        } else if (tag.hasKey("Biomes")) {
            final short[] out = chunkMixin.getBiome16BArray();
            final byte[] data = tag.getByteArray("Biomes");
            for (int i = 0; i < out.length; i++) {
                out[i] = (short) (data[i] & 0xFF);
            }
        } else {
            assert false;
        }
    }

    @Redirect(
            method = "readChunkFromNBT",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/nbt/NBTTagCompound;hasKey(Ljava/lang/String;I)Z",
                    ordinal = 1),
            require = 1)
    private boolean neid$cancelOldChunkBiomeArrayRead(NBTTagCompound tag, String s, int i) {
        return false;
    }

}
