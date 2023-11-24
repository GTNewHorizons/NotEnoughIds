package com.gtnewhorizons.neid.mixins.early.minecraft;

import net.minecraft.network.play.server.S26PacketMapChunkBulk;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(S26PacketMapChunkBulk.class)
public class MixinS26PacketMapChunkBulk {

    @ModifyConstant(method = "readPacketData", constant = @Constant(intValue = 8192), require = 1)
    private static int notenoughIDs$S26PacketReadPacketConstantUpdate(int i) {
        return 12288;
    }
}
