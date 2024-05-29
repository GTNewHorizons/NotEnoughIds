package com.gtnewhorizons.neid;

import net.minecraft.world.biome.BiomeGenBase;

public class PlaceholderBiome extends BiomeGenBase {

    public PlaceholderBiome(int id, BiomeGenBase parent) {
        super(id);
        biomeName = "NotEnoughIDs ID conflict avoidance placeholder for ID " + parent.biomeID
                + " ["
                + parent.getClass().getName()
                + "]";
    }
}
