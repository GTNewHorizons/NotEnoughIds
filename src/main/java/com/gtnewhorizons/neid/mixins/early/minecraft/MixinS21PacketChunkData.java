package com.gtnewhorizons.neid.mixins.early.minecraft;

import net.minecraft.network.play.server.S21PacketChunkData;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.gtnewhorizons.neid.mixins.interfaces.IExtendedBlockStorageMixin;

@Mixin(S21PacketChunkData.class)
public class MixinS21PacketChunkData {

    @Shadow
    private static byte[] field_149286_i;

    @Inject(method = "<init>()V", at = @At(value = "RETURN"), require = 1)
    private void notenoughIDs$S21PacketChunkDataConstructorAddition(CallbackInfo CI) {
        field_149286_i = new byte[229632];
    }

    @ModifyConstant(method = "func_149275_c()I", constant = @Constant(intValue = 196864), require = 1)
    private static int notenoughIDs$S21PacketChunkDataOverride1(int i) {
        return 229632;
    }

    @ModifyConstant(method = "readPacketData", constant = @Constant(intValue = 12288), require = 1)
    private static int notenoughIDs$S21PacketChunkDataOverride2(int i) {
        return 14336;
    }

    @Redirect(
            method = "func_149269_a",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/chunk/storage/ExtendedBlockStorage;getBlockLSBArray()[B"),
            require = 1)
    private static byte[] notenoughIDs$S21PacketChunkDataRedirectGetLSB(ExtendedBlockStorage ebs) {
        return ((IExtendedBlockStorageMixin) ebs).getBlockData();
    }
}
