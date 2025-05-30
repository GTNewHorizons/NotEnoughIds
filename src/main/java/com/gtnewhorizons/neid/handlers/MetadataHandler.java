package com.gtnewhorizons.neid.handlers;

import com.gtnewhorizons.foundation.BlockPacketInfo;
import com.gtnewhorizons.foundation.api.BlockPacketHandler;
import com.gtnewhorizons.foundation.api.ChunkPacketHandler;
import com.gtnewhorizons.neid.mixins.interfaces.IExtendedBlockStorageMixin;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

public class MetadataHandler implements ChunkPacketHandler, BlockPacketHandler {

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
                shortBuffer.put(ebsMixin.getBlock16BMetaArray());
            }
        }
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
                shortBuffer.get(ebsMixin.getBlock16BMetaArray());
            }
        }
    }

    @Override
    public void writeBlockPacket(BlockPacketInfo blockPacketInfo, PacketBuffer packetBuffer) {
        packetBuffer.writeShort(blockPacketInfo.getMetadata());
    }

    @Override
    public void readBlockPacket(BlockPacketInfo blockPacketInfo, PacketBuffer packetBuffer) {
        blockPacketInfo.setMetadata(packetBuffer.readShort());
    }
}
