package com.gtnewhorizons.neid.mixins;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;

import com.gtnewhorizons.neid.mixins.interfaces.IChunkMixin;

public class ChunkProviderPatchHelper {

    public static final byte[] ZERO_LENGTH_BYTE_ARRAY = new byte[0];

    public static byte[] getBiomeArrayReplacer(Chunk chunk, BiomeGenBase[] biomesForGeneration) {
        short[] chunkBiomes = ((IChunkMixin) chunk).getBiome16BArray();

        for (int i = 0; i < chunkBiomes.length; i++) {
            chunkBiomes[i] = (short) biomesForGeneration[i].biomeID;
        }

        return ZERO_LENGTH_BYTE_ARRAY;
    }
}
