package com.gtnewhorizons.neid.mixins.early.minecraft;

import net.minecraft.network.play.server.S24PacketBlockAction;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import com.gtnewhorizons.neid.Constants;

@Mixin(S24PacketBlockAction.class)
public class MixinS24PacketBlockAction {

    @ModifyConstant(
            method = "readPacketData",
            constant = @Constant(intValue = Constants.VANILLA_BLOCK_ID_MASK),
            require = 1)
    private static int neid$readPacketConstantUpdate(int i) {
        return Constants.BLOCK_ID_MASK;
    }

    @ModifyConstant(
            method = "writePacketData",
            constant = @Constant(intValue = Constants.VANILLA_BLOCK_ID_MASK),
            require = 1)
    private static int neid$writePacketConstantUpdate(int i) {
        return Constants.BLOCK_ID_MASK;
    }

}
