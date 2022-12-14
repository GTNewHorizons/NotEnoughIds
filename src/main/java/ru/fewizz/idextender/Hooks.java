package ru.fewizz.idextender;

import java.nio.*;
import net.minecraft.block.*;
import net.minecraft.init.*;
import net.minecraft.nbt.*;
import net.minecraft.world.chunk.storage.*;

public class Hooks {
    public static byte[] getBlockData(final ExtendedBlockStorage ebs) {
        final short[] data = get(ebs);
        final byte[] ret = new byte[data.length * 2];
        ByteBuffer.wrap(ret).asShortBuffer().put(data);
        return ret;
    }

    public static void setBlockData(final ExtendedBlockStorage ebs, final byte[] data, final int offset) {
        ShortBuffer.wrap(get(ebs)).put(ByteBuffer.wrap(data, offset, 8192).asShortBuffer());
    }

    public static void writeChunkToNbt(final NBTTagCompound nbt, final ExtendedBlockStorage ebs) {
        nbt.setByteArray("Blocks16", getBlockData(ebs));
        if (IEConfig.postNeidWorldsSupport) {
            final short[] data = get(ebs);
            final byte[] lsbData = new byte[data.length];
            byte[] msbData = null;
            for (int i = 0; i < data.length; ++i) {
                final int id = data[i] & 0xFFFF;
                if (id <= 255) {
                    lsbData[i] = (byte) id;
                } else if (id <= 4095) {
                    if (msbData == null) {
                        msbData = new byte[data.length / 2];
                    }
                    lsbData[i] = (byte) id;
                    if (i % 2 == 0) {
                        final byte[] array = msbData;
                        final int n = i / 2;
                        array[n] |= (byte) (id >>> 8 & 0xF);
                    } else {
                        final byte[] array2 = msbData;
                        final int n2 = i / 2;
                        array2[n2] |= (byte) (id >>> 4 & 0xF0);
                    }
                }
            }
            nbt.setByteArray("Blocks", lsbData);
            if (msbData != null) {
                nbt.setByteArray("Add", msbData);
            }
        }
    }

    public static void readChunkFromNbt(final ExtendedBlockStorage ebs, final NBTTagCompound nbt) {
        if (nbt.hasKey("Blocks16")) {
            setBlockData(ebs, nbt.getByteArray("Blocks16"), 0);
        } else if (nbt.hasKey("Blocks")) {
            final short[] out = get(ebs);
            final byte[] lsbData = nbt.getByteArray("Blocks");
            if (nbt.hasKey("Add")) {
                final byte[] msbData = nbt.getByteArray("Add");
                for (int i = 0; i < out.length; i += 2) {
                    final byte msPart = msbData[i / 2];
                    out[i] = (short) ((lsbData[i] & 0xFF) | (msPart & 0xF) << 8);
                    out[i + 1] = (short) ((lsbData[i + 1] & 0xFF) | (msPart & 0xF0) << 4);
                }
            } else {
                for (int j = 0; j < out.length; ++j) {
                    out[j] = (short) (lsbData[j] & 0xFF);
                }
            }
        } else {
            assert false;
        }
    }

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
                    if (IEConfig.removeInvalidBlocks) {
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
        if (IEConfig.catchUnregisteredBlocks && id == -1) {
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
