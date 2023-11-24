package com.gtnewhorizons.neid.mixins.early.minecraft;

import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import com.gtnewhorizons.neid.Constants;

@Mixin(World.class)
public class MixinWorld {

    /**
     * Overrides an argument within World.breakBlock which bit-shifts the metadata by 12 bits to shift by 16 bits to
     * accommodate resized block IDs.
     *
     * @param original The original bit shift amount
     * @return Bit shift amount to account for increased size
     */
    @ModifyConstant(method = "func_147480_a(IIIZ)Z", constant = @Constant(intValue = 12), require = 1)
    private static int notenoughIDs$injectedWorldBreakBlock(int original) {
        return Constants.BITS_PER_ID;
    }

}
