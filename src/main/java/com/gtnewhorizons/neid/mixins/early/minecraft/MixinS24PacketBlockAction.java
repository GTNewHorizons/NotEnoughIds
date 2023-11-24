package com.gtnewhorizons.neid.mixins.early.minecraft;

import net.minecraft.network.play.server.S24PacketBlockAction;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import com.gtnewhorizons.neid.Constants;

@Mixin(S24PacketBlockAction.class)
public class MixinS24PacketBlockAction {

    @ModifyConstant(method = "readPacketData", constant = @Constant(intValue = 4095), require = 1)
    private static int notenoughIDs$S24PacketReadPacketConstantUpdate(int i) {
        return Constants.BLOCK_ID_MASK;
    }

    @ModifyConstant(method = "writePacketData", constant = @Constant(intValue = 4095), require = 1)
    private static int notenoughIDs$S24PacketWritePacketConstantUpdate(int i) {
        return Constants.BLOCK_ID_MASK;
    }

}
