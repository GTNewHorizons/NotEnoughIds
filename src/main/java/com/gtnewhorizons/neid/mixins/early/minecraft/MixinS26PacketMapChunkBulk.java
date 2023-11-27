package com.gtnewhorizons.neid.mixins.early.minecraft;

import net.minecraft.network.play.server.S26PacketMapChunkBulk;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import com.gtnewhorizons.neid.Constants;

@Mixin(S26PacketMapChunkBulk.class)
public class MixinS26PacketMapChunkBulk {

    @ModifyConstant(
            method = "readPacketData",
            constant = @Constant(intValue = Constants.VANILLA_BYTES_PER_EBS_MINUS_LIGHTING),
            require = 1)
    private static int neid$readPacketConstantUpdate(int i) {
        return Constants.BYTES_PER_EBS_MINUS_LIGHTING;
    }
}
