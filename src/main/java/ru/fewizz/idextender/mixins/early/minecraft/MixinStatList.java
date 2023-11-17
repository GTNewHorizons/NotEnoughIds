package ru.fewizz.idextender.mixins.early.minecraft;

import net.minecraft.stats.StatList;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import ru.fewizz.idextender.asm.Constants;

@Mixin(StatList.class)
public class MixinStatList {

    @ModifyConstant(method = "<clinit>", constant = @Constant(intValue = 4096))
    private static int notenoughIDs$increaseBlockSize(int original) {
        return Constants.maxBlockId;
    }
}
