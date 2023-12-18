package com.gtnewhorizons.neid;

import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

import com.gtnewhorizons.neid.mixins.interfaces.IExtendedBlockStorageMixin;

@SuppressWarnings("unused") // Called by ASM
public class Hooks {

    public static int getBlockId(final ExtendedBlockStorage ebs, final int x, final int y, final int z) {
        return ((IExtendedBlockStorageMixin) ebs).getBlock16BArray()[y << 8 | z << 4 | x] & 0xFFFF;
    }

}
