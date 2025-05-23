package com.gtnewhorizons.neid.mixins.early.minecraft;

import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.S22PacketMultiBlockChange;
import net.minecraft.world.chunk.Chunk;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.gtnewhorizon.gtnhlib.util.data.BlockMeta;
import com.gtnewhorizons.neid.ClientBlockTransformerRegistry;
import com.llamalad7.mixinextras.sugar.Local;

@Mixin(S22PacketMultiBlockChange.class)
public class MixinS22PacketMultiBlockChange {

    @Unique
    private final BlockMeta pooled = new BlockMeta(Blocks.air, 0);

    /**
     * These values are the number of bytes per block. These are not necessarily the same as within the other
     * calculations. The equation for this number is: BYTES_PER_ID + BYTES_PER_METADATA + BYTES_PER_SKYLIGHT +
     * BYTES_PER_BLOCKLIGHT In vanilla: 1 + 1(half of this byte is also block ID data) + 1 + 1 = 4 Our equation: 2 + 2 +
     * 1 + 1 = 6. This is not necessarily the same as the calculation in other EBS/Chunk datas, as technically the
     * skylight and blocklight data only needs 0.5 bytes, but for the purposes of this packet, they need to take an
     * entire byte.
     */
    @ModifyConstant(
            method = "<init>(I[SLnet/minecraft/world/chunk/Chunk;)V",
            constant = @Constant(intValue = 4),
            require = 1)
    private static int neid$overrideInConstructor(int i) {
        return 6;
    }

    @Redirect(
            method = "<init>(I[SLnet/minecraft/world/chunk/Chunk;)V",
            at = @At(value = "INVOKE", target = "Ljava/io/DataOutputStream;writeShort(I)V", ordinal = 1),
            require = 1)
    private void neid$writeShortRedirectInConstructor(DataOutputStream dataOutputStream, int i,
            @Local Chunk p_i45181_3_, @Local(ordinal = 3) int l, @Local(ordinal = 4) int i1, @Local(ordinal = 5) int j1)
            throws IOException {
        Block block = p_i45181_3_.getBlock(l, j1, i1);
        int meta = p_i45181_3_.getBlockMetadata(l, j1, i1);

        pooled.setBlock(block);
        pooled.setBlockMeta(meta);

        boolean didSomething = ClientBlockTransformerRegistry.transformBlock(
                p_i45181_3_.worldObj,
                l + p_i45181_3_.xPosition * 16,
                j1,
                i1 + p_i45181_3_.zPosition * 16,
                pooled);

        if (didSomething) {
            block = pooled.getBlock();
            meta = pooled.getBlockMeta();
        }

        dataOutputStream.writeShort(Block.getIdFromBlock(block));
        dataOutputStream.writeShort(meta);
    }
}
