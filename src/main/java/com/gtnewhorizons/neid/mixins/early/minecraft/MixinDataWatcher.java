package com.gtnewhorizons.neid.mixins.early.minecraft;

import net.minecraft.entity.DataWatcher;
import net.minecraft.network.PacketBuffer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

import com.gtnewhorizons.neid.Constants;
import com.llamalad7.mixinextras.sugar.Local;

import io.netty.buffer.ByteBuf;

@Mixin(DataWatcher.class)
public class MixinDataWatcher {

    @ModifyConstant(method = "addObject", constant = @Constant(intValue = 31))
    private int neid$addObject_constant(int constant) {
        return Constants.maxDataWatcherId;
    }

    @Redirect(
            method = "func_151509_a",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/PacketBuffer;writeByte(I)Lio/netty/buffer/ByteBuf;"))
    private ByteBuf neid$func_151509_a_ReadByte_To_Short(PacketBuffer instance, int p_writeByte_1_) {
        return instance.writeShort(Constants.MAX_BLOCK_ID);
    }

    // ignore error, it compiles and runs fine
    @ModifyVariable(method = "writeWatchableObjectToPacketBuffer", at = @At(value = "STORE"), name = "i")
    private static int neid$writeWatchableObjectToPacketBuffer_variable_i(int i,
            @Local DataWatcher.WatchableObject p_151510_1_) {
        return (p_151510_1_.getObjectType() << 7 | p_151510_1_.getDataValueId() & Constants.maxDataWatcherId) & 1023;
    }

    @Redirect(
            method = "writeWatchableObjectToPacketBuffer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/PacketBuffer;writeByte(I)Lio/netty/buffer/ByteBuf;",
                    ordinal = 0))
    private static ByteBuf neid$writeWatchableObjectToPacketBuffer_ReadByte_To_Short(PacketBuffer instance,
            int p_writeByte_1_) {
        return instance.writeShort(Constants.MAX_BLOCK_ID);
    }

    @Redirect(
            method = "writeWatchedListToPacketBuffer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/PacketBuffer;writeByte(I)Lio/netty/buffer/ByteBuf;"))
    private static ByteBuf neid$writeWatchedListToPacketBuffer_ReadByte_To_Short(PacketBuffer instance,
            int p_writeByte_1_) {
        return instance.writeShort(Constants.MAX_BLOCK_ID);
    }

    @Redirect(
            method = "readWatchedListFromPacketBuffer",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/network/PacketBuffer;readByte()B"),
            slice = @Slice(from = @At("HEAD"), to = @At(value = "INVOKE", target = "Ljava/util/ArrayList;<init>()V")))
    private static byte neid$readWatchedListFromPacketBuffer_ReadByte_To_Short(PacketBuffer instance) {
        return (byte) instance.readShort();
    }

    @ModifyConstant(method = "readWatchedListFromPacketBuffer", constant = @Constant(intValue = 127))
    private static int neid$readWatchedListFromPacketBuffer_Constant(int constant) {
        return Constants.MAX_BLOCK_ID;
    }

    @ModifyVariable(method = "readWatchedListFromPacketBuffer", at = @At("STORE"), name = "i")
    private static int neid$readWatchedListFromPacketBuffer_variable_i(int i, @Local byte b0) {
        return (b0 & 896) >> 7;
    }

    @ModifyVariable(method = "readWatchedListFromPacketBuffer", at = @At("STORE"), name = "j")
    private static int neid$readWatchedListFromPacketBuffer_variable_j(int j, @Local byte b0) {
        return b0 & Constants.maxDataWatcherId;
    }
}
