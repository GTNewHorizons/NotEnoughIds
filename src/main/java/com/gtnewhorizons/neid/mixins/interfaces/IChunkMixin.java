package com.gtnewhorizons.neid.mixins.interfaces;

public interface IChunkMixin {

    byte[] getBiomeData();

    void setBiomeData(byte[] data, int offset);

    short[] getBiome16BArray();

    void setBiome16BArray(short[] biomeArray);
}
