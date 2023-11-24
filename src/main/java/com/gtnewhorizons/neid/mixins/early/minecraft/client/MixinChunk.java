package com.gtnewhorizons.neid.mixins.early.minecraft.client;

import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.gtnewhorizons.neid.mixins.interfaces.IExtendedBlockStorageMixin;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;

@Mixin(Chunk.class)
public class MixinChunk {

    @Shadow
    private ExtendedBlockStorage[] storageArrays;

    @Redirect(
            method = "fillChunk",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/chunk/storage/ExtendedBlockStorage;getBlockLSBArray()[B"), require = 1)
    private byte[] neid$injectNewDataCopy(ExtendedBlockStorage ebs, @Local(ordinal = 0) byte[] thebytes,
            @Local(ordinal = 3) int forIndex, @Local(ordinal = 2) int offset) {
        IExtendedBlockStorageMixin ebsMixin = (IExtendedBlockStorageMixin) ebs;
        ShortBuffer.wrap(ebsMixin.getBlock16BArray()).put(ByteBuffer.wrap(thebytes, offset, 8192).asShortBuffer());
        return new byte[0];
    }

    @WrapWithCondition(
            method = "fillChunk",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/lang/System;arraycopy(Ljava/lang/Object;ILjava/lang/Object;II)V",
                    ordinal = 0), require = 1)
    private boolean neid$cancelArrayCopy(Object a, int i, Object b, int j, int k) {
        return false;
    }

    @ModifyVariable(method = "fillChunk", at = @At(value = "STORE", ordinal = 1), ordinal = 2, require = 1)
    private int neid$cancelOffsetIncrement(int i, @Local(ordinal = 2) int old) {
        return old + 8192;
    }

    @ModifyConstant(method = "fillChunk", constant = @Constant(intValue = 0, ordinal = 10), require = 1)
    private int neid$NukeMSBForLoop(int i) {
        return this.storageArrays.length + 1;
    }
}
