package com.gtnewhorizons.neid.handlers;

import com.gtnewhorizons.foundation.api.ChunkPacketHandler;
import com.gtnewhorizons.neid.mixins.interfaces.IExtendedBlockStorageMixin;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

public class IDHandler implements ChunkPacketHandler {

    @Override
    public int maxBytesPerChunk() {
        return 131072; // 4096(Blocks in a subchunk) * 16(subchunks in a chunk) * 2 bytes per ID
    }

    @Override
    public void writeChunkPacket(Chunk chunk, boolean sendUpdates, int flagSubChunks, ByteBuffer byteBuffer) {
        ExtendedBlockStorage[] ebsArray = chunk.getBlockStorageArray();
        ShortBuffer shortBuffer = byteBuffer.asShortBuffer();
        for (int i = 0; i < ebsArray.length; i++) {
            ExtendedBlockStorage ebs = ebsArray[i];
            if (ebs != null && (!sendUpdates || !ebs.isEmpty()) && (flagSubChunks & 1 << i) != 0) {
                IExtendedBlockStorageMixin ebsMixin = (IExtendedBlockStorageMixin) ebs;
                shortBuffer.put(ebsMixin.getBlock16BArray());
            }
        }
        // Foundation uses the position of the provided ByteBuffer to know the real size of our data. Because
        // we operate on a ShortBuffer view, the position is not automatically updated for the underlying ByteBuffer.
        byteBuffer.position(shortBuffer.position() * 2);
    }

    @Override
    public void readChunkPacket(Chunk chunk, boolean sendUpdates, int flagSubChunks, ByteBuffer byteBuffer) {
        ExtendedBlockStorage[] ebsArray = chunk.getBlockStorageArray();
        ShortBuffer shortBuffer = byteBuffer.asShortBuffer();
        for (int i = 0; i < ebsArray.length; i++) {
            ExtendedBlockStorage ebs = ebsArray[i];
            if (ebs != null && (flagSubChunks & 1 << i) != 0) {
                IExtendedBlockStorageMixin ebsMixin = (IExtendedBlockStorageMixin) ebs;
                shortBuffer.get(ebsMixin.getBlock16BArray());
            }
        }
    }
}
