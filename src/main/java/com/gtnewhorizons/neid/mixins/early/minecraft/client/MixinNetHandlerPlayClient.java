package com.gtnewhorizons.neid.mixins.early.minecraft.client;

import java.io.DataInputStream;
import java.io.IOException;

import net.minecraft.block.Block;
import net.minecraft.client.network.NetHandlerPlayClient;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.llamalad7.mixinextras.sugar.Local;

@Mixin(NetHandlerPlayClient.class)
public class MixinNetHandlerPlayClient {

    @Redirect(
            method = "handleMultiBlockChange",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/Block;getBlockById(I)Lnet/minecraft/block/Block;"), require = 1)
    private Block neid$ReadShortOverride(int i, @Local(ordinal = 1) short short2) {
        return Block.getBlockById(short2);
    }

    @ModifyArg(
            method = "handleMultiBlockChange",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/multiplayer/WorldClient;func_147492_c(IIILnet/minecraft/block/Block;I)Z"),
            index = 4, require = 1)
    private int test(int i, @Local DataInputStream datainputstream) throws IOException {
        return datainputstream.readByte() & 15;
    }

}
