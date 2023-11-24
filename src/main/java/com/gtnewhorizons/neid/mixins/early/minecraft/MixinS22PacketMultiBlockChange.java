package com.gtnewhorizons.neid.mixins.early.minecraft;

import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.block.Block;
import net.minecraft.network.play.server.S22PacketMultiBlockChange;
import net.minecraft.world.chunk.Chunk;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.llamalad7.mixinextras.sugar.Local;

@Mixin(S22PacketMultiBlockChange.class)
public class MixinS22PacketMultiBlockChange {

    @ModifyConstant(
            method = "<init>(I[SLnet/minecraft/world/chunk/Chunk;)V",
            constant = @Constant(intValue = 4),
            require = 1)
    private static int notenoughIDs$overrideJinPacketMultiBlockChangeConstructor(int i) {
        return 5;
    }

    @Redirect(
            method = "<init>(I[SLnet/minecraft/world/chunk/Chunk;)V",
            at = @At(value = "INVOKE", target = "Ljava/io/DataOutputStream;writeShort(I)V", ordinal = 1),
            require = 1)
    private void notenoughIDs$S22PacketWriteShortRedirectInConstructor(DataOutputStream dataOutputStream, int i,
            @Local Chunk p_i45181_3_, @Local(ordinal = 3) int l, @Local(ordinal = 4) int i1, @Local(ordinal = 5) int j1)
            throws IOException {
        dataOutputStream.writeShort(Block.getIdFromBlock(p_i45181_3_.getBlock(l, j1, i1)));
        dataOutputStream.writeByte(p_i45181_3_.getBlockMetadata(l, j1, i1));
    }
}
