package com.gtnewhorizons.neid.mixins.interfaces;

public interface IExtendedBlockStorageMixin {

    short[] getBlock16BArray();

    byte[] getBlockData();

    void setBlockData(byte[] data, int offset);

}
