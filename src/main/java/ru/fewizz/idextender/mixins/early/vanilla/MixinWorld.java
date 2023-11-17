package ru.fewizz.idextender.mixins.early.vanilla;

import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(World.class)
public class MixinWorld {

    /**
     * Overrides an argument within World.breakBlock which bit-shifts the metadata by 12 bits to shift by 16 bits to
     * accommodate resized block IDs.
     *
     * @param i The block ID + bit-shifted metadata received from the original MC argument
     * @return The newly bit-shifted by 16 value
     */
    @ModifyArg(
            method = "func_147480_a(IIIZ)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;playAuxSFX(IIIII)V"),
            index = 4)
    private int notenoughIDs$injectedWorldBreakBlock(int i) {
        int blockId = i & 4095;
        int blockMeta = (i - blockId) >> 12;
        return blockId + (blockMeta << 16);
    }

}
