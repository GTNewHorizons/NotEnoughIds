package com.gtnewhorizons.neid.mixins.early.minecraft;

import net.minecraft.network.play.server.S21PacketChunkData;
import net.minecraft.world.chunk.NibbleArray;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.gtnewhorizons.neid.Constants;
import com.gtnewhorizons.neid.mixins.interfaces.IExtendedBlockStorageMixin;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;

@Mixin(S21PacketChunkData.class)
public class MixinS21PacketChunkData {

    private static final byte[] fakeByteArray = new byte[0];
    private static final NibbleArray fakeNibbleArray = new NibbleArray(0, 0);

    @ModifyConstant(
            method = "<clinit>",
            constant = @Constant(intValue = Constants.VANILLA_BYTES_PER_CHUNK),
            require = 1)
    private static int neid$OverrideBytesPerChunk1(int old) {
        return Constants.BYTES_PER_CHUNK;
    }

    @ModifyConstant(
            method = "func_149275_c()I",
            constant = @Constant(intValue = Constants.VANILLA_BYTES_PER_CHUNK),
            require = 1)
    private static int neid$OverrideBytesPerChunk2(int i) {
        return Constants.BYTES_PER_CHUNK;
    }

    @ModifyConstant(
            method = "readPacketData",
            constant = @Constant(intValue = Constants.VANILLA_BYTES_PER_EBS),
            require = 1)
    private static int neid$OverrideBytesPerEBS(int i) {
        return Constants.BYTES_PER_EBS;
    }

    @Redirect(
            method = "func_149269_a",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/chunk/storage/ExtendedBlockStorage;getBlockLSBArray()[B"),
            require = 1)
    private static byte[] neid$injectNewDataCopy(ExtendedBlockStorage ebs, @Local(ordinal = 0) byte[] thebytes,
            @Local(ordinal = 1) LocalIntRef offset) {
        IExtendedBlockStorageMixin ebsMixin = (IExtendedBlockStorageMixin) ebs;
        byte[] data = ebsMixin.getBlockData();
        System.arraycopy(data, 0, thebytes, offset.get(), Constants.BLOCKS_PER_EBS * 2);
        offset.set(offset.get() + (Constants.BLOCKS_PER_EBS * 2));
        return fakeByteArray;
    }

    @WrapWithCondition(
            method = "func_149269_a",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/lang/System;arraycopy(Ljava/lang/Object;ILjava/lang/Object;II)V",
                    ordinal = 0),
            require = 1)
    private static boolean neid$cancelLSBArrayCopy(Object a, int i, Object b, int j, int k) {
        return false;
    }

    @Redirect(
            method = "func_149269_a",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/chunk/storage/ExtendedBlockStorage;getMetadataArray()Lnet/minecraft/world/chunk/NibbleArray;"),
            require = 1)
    private static NibbleArray neid$injectNewMetadataCopy(ExtendedBlockStorage ebs, @Local(ordinal = 0) byte[] thebytes,
            @Local(ordinal = 1) LocalIntRef offset) {
        IExtendedBlockStorageMixin ebsMixin = (IExtendedBlockStorageMixin) ebs;
        byte[] meta = ebsMixin.getBlockMeta();
        System.arraycopy(meta, 0, thebytes, offset.get(), Constants.BLOCKS_PER_EBS * 2);
        offset.set(offset.get() + (Constants.BLOCKS_PER_EBS * 2));
        return fakeNibbleArray;
    }

    @WrapWithCondition(
            method = "func_149269_a",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/lang/System;arraycopy(Ljava/lang/Object;ILjava/lang/Object;II)V",
                    ordinal = 1),
            require = 1)
    private static boolean neid$cancelMetadataArrayCopy(Object a, int i, Object b, int j, int k) {
        return false;
    }

    @Redirect(
            method = "func_149269_a",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/chunk/storage/ExtendedBlockStorage;getBlockMSBArray()Lnet/minecraft/world/chunk/NibbleArray;",
                    ordinal = 0),
            require = 1)
    private static NibbleArray neid$nukeMSBLoop(ExtendedBlockStorage ebs) {
        return null;
    }

}
