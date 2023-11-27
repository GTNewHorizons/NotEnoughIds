package com.gtnewhorizons.neid.mixins.early.minecraft;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.gtnewhorizons.neid.Constants;
import com.gtnewhorizons.neid.NEIDConfig;
import com.gtnewhorizons.neid.mixins.interfaces.IExtendedBlockStorageMixin;
import com.llamalad7.mixinextras.sugar.Local;

@Mixin(AnvilChunkLoader.class)
public class MixinAnvilChunkLoader {

    @Redirect(
            method = "writeChunkToNBT",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/nbt/NBTTagCompound;setByteArray(Ljava/lang/String;[B)V",
                    ordinal = 0),
            require = 1)
    private void neid$overrideSetByteArray(NBTTagCompound nbt, String s, byte[] oldbrokenbytes,
            @Local(ordinal = 0) ExtendedBlockStorage ebs) {
        IExtendedBlockStorageMixin ebsMixin = (IExtendedBlockStorageMixin) ebs;
        nbt.setByteArray("Blocks16", ebsMixin.getBlockData());
        if (NEIDConfig.postNeidWorldsSupport) {
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
            method = "readChunkFromNBT",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/chunk/storage/ExtendedBlockStorage;setBlockLSBArray([B)V"),
            require = 1)
    private void neid$overrideSetLSBArray(ExtendedBlockStorage ebs, byte[] oldbrokenbytes,
            @Local(ordinal = 1) NBTTagCompound nbt) {
        IExtendedBlockStorageMixin ebsMixin = (IExtendedBlockStorageMixin) ebs;
        if (nbt.hasKey("Blocks16")) {
            ebsMixin.setBlockData(nbt.getByteArray("Blocks16"), 0);
        } else if (nbt.hasKey("Blocks")) {
            final short[] out = ebsMixin.getBlock16BArray();
            final byte[] lsbData = nbt.getByteArray("Blocks");
            if (nbt.hasKey("Add")) {
                final byte[] msbData = nbt.getByteArray("Add");
                for (int i = 0; i < out.length; i += 2) {
                    final byte msPart = msbData[i / 2];
                    out[i] = (short) ((lsbData[i] & 0xFF) | (msPart & 0xF) << 8);
                    out[i + 1] = (short) ((lsbData[i + 1] & 0xFF) | (msPart & 0xF0) << 4);
                }
            } else {
                for (int j = 0; j < out.length; ++j) {
                    out[j] = (short) (lsbData[j] & 0xFF);
                }
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
                    ordinal = 0),
            require = 1)
    private boolean neid$nukeMSBCheck(NBTTagCompound nbttagcompound1, String s, int i) {
        return false;
    }
}
