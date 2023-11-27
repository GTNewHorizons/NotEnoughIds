package com.gtnewhorizons.neid.mixins.early.minecraft;

import net.minecraft.server.management.ItemInWorldManager;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import com.gtnewhorizons.neid.Constants;

@Mixin(ItemInWorldManager.class)
public class MixinItemInWorldManager {

    @ModifyConstant(
            method = "tryHarvestBlock",
            constant = @Constant(intValue = Constants.VANILLA_BITS_PER_ID),
            require = 1)
    private static int neid$newBitsPerID(int i) {
        return Constants.BITS_PER_ID;
    }

}
