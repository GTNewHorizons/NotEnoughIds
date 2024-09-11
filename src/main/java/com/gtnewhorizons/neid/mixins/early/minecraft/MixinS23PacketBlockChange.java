package com.gtnewhorizons.neid.mixins.early.minecraft;

import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.S23PacketBlockChange;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import io.netty.buffer.ByteBuf;

@Mixin(S23PacketBlockChange.class)
public class MixinS23PacketBlockChange {

    @Redirect(
            method = "readPacketData",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/network/PacketBuffer;readUnsignedByte()S", ordinal = 1),
            require = 1)
    private short neid$redirectMetadataRead(PacketBuffer data) {
        return data.readShort();
    }

    @Redirect(
            method = "writePacketData",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/PacketBuffer;writeByte(I)Lio/netty/buffer/ByteBuf;",
                    ordinal = 1),
            require = 1)
    private ByteBuf neid$redirectMetadataWrite(PacketBuffer data, int i) {
        return data.writeShort(i);
    }

}
