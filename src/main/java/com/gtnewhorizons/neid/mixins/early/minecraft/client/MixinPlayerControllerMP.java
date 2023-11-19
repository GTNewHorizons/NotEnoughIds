package com.gtnewhorizons.neid.mixins.early.minecraft.client;

import com.gtnewhorizons.neid.Constants;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(PlayerControllerMP.class)
public class MixinPlayerControllerMP {

    @ModifyConstant(method = "onPlayerDestroyBlock", constant = @Constant(intValue = 12))
    private static int neid$newBitsPerID(int i) {
        return Constants.BITS_PER_ID;
    }

}
