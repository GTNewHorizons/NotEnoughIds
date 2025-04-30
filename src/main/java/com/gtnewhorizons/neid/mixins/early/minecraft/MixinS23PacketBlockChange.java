package com.gtnewhorizons.neid.mixins.early.minecraft;

import net.minecraft.block.Block;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.gtnewhorizon.gtnhlib.util.data.BlockMeta;
import com.gtnewhorizons.neid.ClientBlockTransformerRegistry;

import io.netty.buffer.ByteBuf;

@Mixin(S23PacketBlockChange.class)
public class MixinS23PacketBlockChange {

    @Shadow
    public Block field_148883_d;
    @Shadow
    public int field_148884_e;

    @Inject(method = "<init>(IIILnet/minecraft/world/World;)V", at = @At("TAIL"))
    public void neid$transformBlock(int x, int y, int z, World world, CallbackInfo ci) {
        BlockMeta bm = new BlockMeta(field_148883_d, field_148884_e);

        if (ClientBlockTransformerRegistry.transformBlock(world, x, y, z, bm)) {
            field_148883_d = bm.getBlock();
            field_148884_e = bm.getBlockMeta();
        }
    }

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
