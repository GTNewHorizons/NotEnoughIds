package com.gtnewhorizons.neid.mixins.interfaces;

import net.minecraft.world.chunk.NibbleArray;

public interface IExtendedBlockStorageMixin {

    short[] getBlock16BArray();

    short[] getBlock16BMetaArray();

    byte[] getBlockData();

    byte[] getBlockMeta();

    NibbleArray getBlockMetaNibble();

    void setBlockData(byte[] data, int offset);

    void setBlockMeta(byte[] data, int offset);

}
