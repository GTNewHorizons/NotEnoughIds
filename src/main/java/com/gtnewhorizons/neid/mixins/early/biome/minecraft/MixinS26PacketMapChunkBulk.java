package com.gtnewhorizons.neid.mixins.early.biome.minecraft;

import net.minecraft.network.play.server.S26PacketMapChunkBulk;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(S26PacketMapChunkBulk.class)
public class MixinS26PacketMapChunkBulk {

    @ModifyConstant(method = "readPacketData", constant = @Constant(intValue = 256))
    private int neid$overrideBiomeBytesPerChunk(int oldValue) {
        return 512;
    }
}
