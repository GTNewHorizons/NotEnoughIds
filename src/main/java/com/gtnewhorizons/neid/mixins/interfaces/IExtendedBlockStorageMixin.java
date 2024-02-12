package com.gtnewhorizons.neid.mixins.interfaces;

public interface IExtendedBlockStorageMixin {

    short[] getBlock16BArray();

    short[] getBlock16BMetaArray();

    byte[] getBlockData();

    byte[] getBlockMeta();

    void setBlockData(byte[] data, int offset);

    void setBlockMeta(byte[] data, int offset);

}
