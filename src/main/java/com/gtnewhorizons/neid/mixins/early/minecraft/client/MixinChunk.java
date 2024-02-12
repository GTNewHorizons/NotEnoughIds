package com.gtnewhorizons.neid.mixins.early.minecraft.client;

import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.NibbleArray;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.gtnewhorizons.neid.Constants;
import com.gtnewhorizons.neid.mixins.interfaces.IExtendedBlockStorageMixin;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;

@Mixin(Chunk.class)
public class MixinChunk {

    private static final byte[] fakeByteArray = new byte[0];
    private static final NibbleArray fakeNibbleArray = new NibbleArray(fakeByteArray, 0);

    @Shadow
    private ExtendedBlockStorage[] storageArrays;

    @Redirect(
            method = "fillChunk",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/chunk/storage/ExtendedBlockStorage;getBlockLSBArray()[B"),
            require = 1)
    private byte[] neid$injectNewDataCopy(ExtendedBlockStorage ebs, @Local(ordinal = 0) byte[] thebytes,
            @Local(ordinal = 2) LocalIntRef offset) {
        IExtendedBlockStorageMixin ebsMixin = (IExtendedBlockStorageMixin) ebs;
        ShortBuffer.wrap(ebsMixin.getBlock16BArray())
                .put(ByteBuffer.wrap(thebytes, offset.get(), Constants.BLOCKS_PER_EBS * 2).asShortBuffer());
        offset.set(offset.get() + (Constants.BLOCKS_PER_EBS * 2));
        return fakeByteArray;
    }

    @Redirect(
            method = "fillChunk",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/chunk/storage/ExtendedBlockStorage;getMetadataArray()Lnet/minecraft/world/chunk/NibbleArray;"),
            require = 1)
    private NibbleArray neid$injectNewMetadataCopy(ExtendedBlockStorage ebs, @Local(ordinal = 0) byte[] thebytes,
            @Local(ordinal = 2) LocalIntRef offset) {
        IExtendedBlockStorageMixin ebsMixin = (IExtendedBlockStorageMixin) ebs;
        ShortBuffer.wrap(ebsMixin.getBlock16BMetaArray())
                .put(ByteBuffer.wrap(thebytes, offset.get(), Constants.BLOCKS_PER_EBS * 2).asShortBuffer());
        offset.set(offset.get() + (Constants.BLOCKS_PER_EBS * 2));
        return fakeNibbleArray;
    }

    @WrapWithCondition(
            method = "fillChunk",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/lang/System;arraycopy(Ljava/lang/Object;ILjava/lang/Object;II)V",
                    ordinal = 0),
            require = 1)
    private boolean neid$cancelLSBArrayCopy(Object a, int i, Object b, int j, int k) {
        return false;
    }

    @WrapWithCondition(
            method = "fillChunk",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/lang/System;arraycopy(Ljava/lang/Object;ILjava/lang/Object;II)V",
                    ordinal = 1),
            require = 1)
    private boolean neid$cancelMetadataArrayCopy(Object a, int i, Object b, int j, int k) {
        return false;
    }

    @ModifyConstant(method = "fillChunk", constant = @Constant(intValue = 0, ordinal = 10), require = 1)
    private int neid$NukeMSBForLoop(int i) {
        return this.storageArrays.length + 1;
    }
}
