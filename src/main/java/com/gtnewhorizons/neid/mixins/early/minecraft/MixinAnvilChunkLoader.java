package com.gtnewhorizons.neid.mixins.early.minecraft;

import java.nio.ByteBuffer;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.chunk.NibbleArray;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.gtnewhorizons.neid.Constants;
import com.gtnewhorizons.neid.NEIDConfig;
import com.gtnewhorizons.neid.mixins.interfaces.IExtendedBlockStorageMixin;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;

@Mixin(AnvilChunkLoader.class)
public class MixinAnvilChunkLoader {

    @Inject(method = "writeChunkToNBT", at = @At("HEAD"))
    private void neid$injectLevelTag(CallbackInfo ci, @Local NBTTagCompound tag) {
        tag.setBoolean("NEID", true);
    }

    @Redirect(
            method = "writeChunkToNBT",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/nbt/NBTTagCompound;setByteArray(Ljava/lang/String;[B)V",
                    ordinal = 0),
            require = 1)
    private void neid$overrideWriteLSBArray(NBTTagCompound nbt, String s, byte[] oldbrokenbytes,
            @Local(ordinal = 0) ExtendedBlockStorage ebs) {
        IExtendedBlockStorageMixin ebsMixin = (IExtendedBlockStorageMixin) ebs;
        nbt.setByteArray("Blocks16", ebsMixin.getBlockData());
        if (NEIDConfig.PostNeidWorldsSupport) {
            final short[] data = ebsMixin.getBlock16BArray();
            final byte[] lsbData = new byte[data.length];
            byte[] msbData = null;
            for (int i = 0; i < data.length; ++i) {
                final int id = data[i] & 0xFFFF;
                if (id <= 255) {
                    lsbData[i] = (byte) id;
                } else if (id <= Constants.VANILLA_MAX_BLOCK_ID) {
                    if (msbData == null) {
                        msbData = new byte[data.length / 2];
                    }
                    lsbData[i] = (byte) id;
                    if (i % 2 == 0) {
                        final byte[] array = msbData;
                        final int n = i / 2;
                        array[n] |= (byte) (id >>> 8 & 0xF);
                    } else {
                        final byte[] array2 = msbData;
                        final int n2 = i / 2;
                        array2[n2] |= (byte) (id >>> 4 & 0xF0);
                    }
                }
            }
            nbt.setByteArray("Blocks", lsbData);
            if (msbData != null) {
                nbt.setByteArray("Add", msbData);
            }
        }
    }

    @Redirect(
            method = "writeChunkToNBT",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/nbt/NBTTagCompound;setByteArray(Ljava/lang/String;[B)V",
                    ordinal = 2),
            require = 1)
    private void neid$overrideWriteMetadataArray(NBTTagCompound nbt, String s, byte[] oldbrokenbytes,
            @Local(ordinal = 0) ExtendedBlockStorage ebs) {
        IExtendedBlockStorageMixin ebsMixin = (IExtendedBlockStorageMixin) ebs;
        nbt.setByteArray("Data16", ebsMixin.getBlockMeta());
        if (NEIDConfig.PostNeidWorldsSupport) {
            final short[] data = ebsMixin.getBlock16BMetaArray();
            final byte[] metaData = new byte[data.length / 2];
            for (int i = 0; i < data.length; i += 2) {
                int meta1 = data[i];
                int meta2 = data[i + 1];

                if (meta1 < 0 || meta1 > 15) {
                    meta1 = 0;
                }
                if (meta2 < 0 || meta2 > 15) {
                    meta2 = 0;
                }

                metaData[i / 2] = (byte) (meta2 << 4 | meta1);
                final int meta = data[i];
            }
            nbt.setByteArray("Data", metaData);
        }
    }

    @Inject(
            method = "checkedReadChunkFromNBT__Async",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/chunk/storage/AnvilChunkLoader;readChunkFromNBT(Lnet/minecraft/world/World;Lnet/minecraft/nbt/NBTTagCompound;)Lnet/minecraft/world/chunk/Chunk;"),
            remap = false)
    private void neid$preprocessOldChunk(CallbackInfoReturnable<Object[]> cir, @Local World world,
            @Local LocalRef<NBTTagCompound> tag) {
        NBTTagCompound level = tag.get().getCompoundTag("Level");

        if (!level.hasKey("NEID")) {
            NBTTagList nbttaglist = level.getTagList("Sections", 10);
            for (int i = 0; i < nbttaglist.tagCount(); i++) {
                NBTTagCompound tag1 = nbttaglist.getCompoundTagAt(i);
                if (tag1.hasKey("Blocks") && !tag1.hasKey("Blocks16")) {
                    final byte[] lsbData = tag1.getByteArray("Blocks");
                    final short[] out = new short[Constants.BLOCKS_PER_EBS];
                    if (tag1.hasKey("Add")) {
                        final byte[] msbData = tag1.getByteArray("Add");
                        for (int j = 0; j < out.length; j += 2) {
                            final byte msPart = msbData[i / 2];
                            out[i] = (short) ((lsbData[i] & 0xFF) | (msPart & 0xF) << 8);
                            out[i + 1] = (short) ((lsbData[i + 1] & 0xFF) | (msPart & 0xF0) << 4);
                        }
                    } else {
                        for (int j = 0; j < out.length; j++) {
                            out[j] = (short) (lsbData[j] & 0xFF);
                        }
                    }
                    final byte[] ret = new byte[out.length * 2];
                    ByteBuffer.wrap(ret).asShortBuffer().put(out);
                    tag1.setByteArray("Blocks16", ret);
                }
                if (tag1.hasKey("Data") && !tag1.hasKey("Data16")) {
                    final byte[] metaData = tag1.getByteArray("Data");
                    final short[] out = new short[Constants.BLOCKS_PER_EBS];
                    for (int j = 0; j < out.length; j += 2) {
                        final byte meta = metaData[j / 2];
                        out[j] = (short) (meta & 0xF);
                        out[j + 1] = (short) ((meta >> 4) & 0xF);
                    }
                    final byte[] ret = new byte[out.length * 2];
                    ByteBuffer.wrap(ret).asShortBuffer().put(out);
                    tag1.setByteArray("Data16", ret);
                }
            }
            level.setBoolean("NEID", true);
        }
    }

    @Redirect(
            method = "readChunkFromNBT",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/chunk/storage/ExtendedBlockStorage;setBlockLSBArray([B)V"),
            require = 1)
    private void neid$overrideReadLSBArray(ExtendedBlockStorage ebs, byte[] oldbrokenbytes,
            @Local(ordinal = 1) NBTTagCompound nbt) {
        IExtendedBlockStorageMixin ebsMixin = (IExtendedBlockStorageMixin) ebs;
        if (nbt.hasKey("Blocks16")) {
            ebsMixin.setBlockData(nbt.getByteArray("Blocks16"), 0);
        } else {
            assert false;
        }
    }

    @Redirect(
            method = "readChunkFromNBT",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/nbt/NBTTagCompound;hasKey(Ljava/lang/String;I)Z",
                    ordinal = 0),
            require = 1)
    private boolean neid$overrideReadMSBArray(NBTTagCompound nbttagcompound1, String s, int i) {
        return false;
    }

    @Redirect(
            method = "readChunkFromNBT",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/chunk/storage/ExtendedBlockStorage;setBlockMetadataArray(Lnet/minecraft/world/chunk/NibbleArray;)V"),
            require = 1)
    private void neid$overrideReadMetadataArray(ExtendedBlockStorage ebs, NibbleArray oldBrokenNibbleArray,
            @Local(ordinal = 1) NBTTagCompound nbt) {
        IExtendedBlockStorageMixin ebsMixin = (IExtendedBlockStorageMixin) ebs;
        if (nbt.hasKey("Data16")) {
            ebsMixin.setBlockMeta(nbt.getByteArray("Data16"), 0);
        } else {
            assert false;
        }
    }
}
