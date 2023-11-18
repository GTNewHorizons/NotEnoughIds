package com.gtnewhorizons.neid.mixins.early.minecraft;

import net.minecraft.block.BlockFire;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import com.gtnewhorizons.neid.Constants;

@Mixin(BlockFire.class)
public class MixinBlockFire {

    private int[] field_149849_a = new int[Constants.MAX_BLOCK_ID];
    private int[] field_149848_b = new int[Constants.MAX_BLOCK_ID];

    @ModifyConstant(
            method = { "rebuildFireInfo()V", "getFlammability(Lnet/minecraft/block/Block;)I",
                    "getEncouragement(Lnet/minecraft/block/Block;)I" },
            constant = @Constant(intValue = 4096),
            remap = false)
    private int notenoughIDs$increaseBlockSize(int original) {
        return Constants.MAX_BLOCK_ID;
    }
}
