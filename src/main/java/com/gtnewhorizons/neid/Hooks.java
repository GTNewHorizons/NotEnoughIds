package com.gtnewhorizons.neid;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

public class Hooks {

    public static int getBlockId(final ExtendedBlockStorage ebs, final int x, final int y, final int z) {
        return get(ebs)[y << 8 | z << 4 | x] & 0xFFFF;
    }

    public static Block getBlock(final ExtendedBlockStorage ebs, final int x, final int y, final int z) {
        return Block.getBlockById(getBlockId(ebs, x, y, z));
    }

    public static void setBlockId(final ExtendedBlockStorage ebs, final int x, final int y, final int z, final int id) {
        get(ebs)[y << 8 | z << 4 | x] = (short) id;
    }

    public static short[] create16BArray() {
        return new short[4096];
    }

    public static short[] get(final ExtendedBlockStorage ebs) {
        return null;
    }

    public static void setTickRefCount(final ExtendedBlockStorage ebs, final int value) {}

    public static void setBlockRefCount(final ExtendedBlockStorage ebs, final int value) {}

    public static void removeInvalidBlocksHook(final ExtendedBlockStorage ebs) {
        final short[] blkIds = get(ebs);
        int cntNonEmpty = 0;
        int cntTicking = 0;
        for (int off = 0; off < blkIds.length; ++off) {
            final int id = blkIds[off] & 0xFFFF;
            if (id > 0) {
                final Block block = (Block) Block.blockRegistry.getObjectById(id);
                if (block == null) {
                    if (NEIDConfig.removeInvalidBlocks) {
                        blkIds[off] = 0;
                    }
                } else if (block != Blocks.air) {
                    ++cntNonEmpty;
                    if (block.getTickRandomly()) {
                        ++cntTicking;
                    }
                }
            }
        }
        setBlockRefCount(ebs, cntNonEmpty);
        setTickRefCount(ebs, cntTicking);
    }

    public static int getIdFromBlockWithCheck(final Block block, final Block oldBlock) {
        final int id = Block.getIdFromBlock(block);
        if (NEIDConfig.catchUnregisteredBlocks && id == -1) {
            throw new IllegalArgumentException(
                    "Block " + block
                            + " is not registered. <-- Say about this to the author of this mod, or you can try to enable \"RemoveInvalidBlocks\" option in NEID config.");
        }
        if (id >= 0 && id <= 32767) {
            return id;
        }
        if (id == -1) {
            return Block.getIdFromBlock(oldBlock);
        }
        throw new IllegalArgumentException("id out of range: " + id);
    }
}
