package ru.fewizz.idextender;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import ru.fewizz.idextender.asm.Constants;

public class Hooks {
    public static byte[] getBlockData(ExtendedBlockStorage ebs) {
        short[] data = get(ebs);
        byte[] ret = new byte[data.length * 2];

        ByteBuffer.wrap(ret).asShortBuffer().put(data);

        return ret;
    }

    public static int handleMultiBlockChange_readBlockIDAdditionalBitsIfNeed(DataInputStream dis, int size, int count) {
        if (size / (Short.BYTES * 2 + Byte.BYTES) == count) {
            try {
                return dis.readByte();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (size / (Short.BYTES * 2) == count) { // Vanilla
            return 0;
        }
        throw new RuntimeException();
    }

    public static void setBlockData(ExtendedBlockStorage ebs, byte[] data, int offset) {
        ShortBuffer.wrap(get(ebs))
                .put(ByteBuffer.wrap(data, offset, Constants.ebsIdArraySize).asShortBuffer());
    }

    public static void grabDataFromChunkBulkPacket(
            byte[] overallData, int size, byte[][] data, int[] chunks, boolean hasSky) {
        int old = (4096 + 2 * 2048 + (hasSky ? 2048 : 0)); // Without MSB
        int nw = old + 4096;
        int step = -1;

        int count = 0; // Recalculating this every time, meh...
        for (int c = 0; c < chunks.length; c++) {
            for (int i = 0; i < 16; i++) {
                count += (chunks[c] >> i) & 1; // Could better, but not so critical
            }
        }

        if (old * count + chunks.length * 256 == size) step = old;
        else if (nw * count + chunks.length * 256 == size) step = nw;
        else throw new RuntimeException("Something is wrong with chunk bulk packet");

        int off = 0;

        for (int c = 0; c < chunks.length; c++) {
            count = 0;
            for (int i = 0; i < 16; i++) {
                count += (chunks[c] >> i) & 1; // Could better, but not so critical
            }
            size = step * count + 256;
            data[c] = new byte[size];

            System.arraycopy(overallData, off, data[c], 0, size);
            off += size;
        }
    }

    public static int copyBlockDataFromPacket(Chunk c, byte[] data, int bits, boolean affectTop) {
        int resultingOldSize = 0;
        int resultingNewSize = 0;

        int oldAddition = 4096 + 2048 * 2 + (!c.worldObj.provider.hasNoSky ? 2048 : 0);
        int newAddition = oldAddition + 4096;

        for (int e = 0; e < 16; e++) {
            if (((bits >> e) & 1) == 1) {
                if (c.getBlockStorageArray()[e] == null)
                    c.getBlockStorageArray()[e] = new ExtendedBlockStorage(e << 4, !c.worldObj.provider.hasNoSky);
                resultingOldSize += oldAddition;
                resultingNewSize += newAddition;
            } else if (affectTop && c.getBlockStorageArray()[e] != null) { // Vanilla like behavior
                c.getBlockStorageArray()[e] = null;
            }
        }

        if (affectTop) {
            resultingNewSize += 256;
            resultingOldSize += 256;
        }

        int offset = 0;

        if (data.length == resultingNewSize) {
            for (int e = 0; e < 16; e++) {
                if (((bits >> e) & 1) == 0) continue;
                setBlockData(c.getBlockStorageArray()[e], data, offset);
                offset += 16 * 16 * 16 * Short.BYTES;
            }
        } else if (data.length == resultingOldSize) {
            // Hmm, ok
            for (int e = 0; e < 16; e++) {
                if (((bits >> e) & 1) == 0) continue;

                short[] data0 = get(c.getBlockStorageArray()[e]);
                for (int i = 0; i < 16 * 16 * 16; i++) data0[i] = (short) (data[offset + i] & 0xFF);
                offset += 16 * 16 * 16;
            }
        } else {
            throw new RuntimeException("Oops, looks like invalid chunk data. The size is: "
                    + data.length + ", but should be either "
                    + resultingOldSize + " or " + resultingNewSize
                    + ". Report to NEID dev. Thx.");
        }
        return offset;
    }

    public static void writeChunkToNbt(NBTTagCompound nbt, ExtendedBlockStorage ebs) {
        nbt.setByteArray("Blocks16", getBlockData(ebs));

        // save id in legacy format to leave worlds as intact as possible after removing NEIDs (only if this option is
        // enabled in config file.)
        if (IEConfig.postNeidWorldsSupport) {
            short[] data = get(ebs);
            byte[] lsbData = new byte[data.length];
            byte[] msbData = null;

            for (int i = 0; i < data.length; i++) {
                int id = data[i] & 0xffff;

                if (id <= 0xff) {
                    lsbData[i] = (byte) id;
                } else if (id <= 0xfff) {
                    if (msbData == null) {
                        msbData = new byte[data.length / 2];
                    }

                    lsbData[i] = (byte) id;

                    if (i % 2 == 0) {
                        msbData[i / 2] |= (id >>> 8) & 0x0f;
                    } else {
                        msbData[i / 2] |= (id >>> 4) & 0xf0;
                    }
                } else {
                    // ignore id, treat as 0 (air)
                }
            }

            nbt.setByteArray("Blocks", lsbData);

            if (msbData != null) {
                nbt.setByteArray("Add", msbData);
            }
        }
    }

    public static void readChunkFromNbt(ExtendedBlockStorage ebs, NBTTagCompound nbt) {
        if (nbt.hasKey("Blocks16")) {
            setBlockData(ebs, nbt.getByteArray("Blocks16"), 0);
        } else if (nbt.hasKey("Blocks")) {
            short[] out = get(ebs);
            byte[] lsbData = nbt.getByteArray("Blocks");

            if (nbt.hasKey("Add")) {
                byte[] msbData = nbt.getByteArray("Add");

                for (int i = 0; i < out.length; i += 2) {
                    byte msPart = msbData[i / 2];

                    out[i] = (short) (lsbData[i] & 0xff | (msPart & 0x0f) << 8);
                    out[i + 1] = (short) (lsbData[i + 1] & 0xff | (msPart & 0xf0) << 4);
                }
            } else {
                for (int i = 0; i < out.length; i++) {
                    out[i] = (short) (lsbData[i] & 0xff);
                }
            }
        } else {
            assert false;
        }
    }

    public static int getBlockId(ExtendedBlockStorage ebs, int x, int y, int z) {
        return get(ebs)[y << 8 | z << 4 | x] & Constants.BLOCK_ID_MASK;
    }

    public static Block getBlock(ExtendedBlockStorage ebs, int x, int y, int z) {
        return Block.getBlockById(getBlockId(ebs, x, y, z));
    }

    public static void setBlockId(ExtendedBlockStorage ebs, int x, int y, int z, int id) {
        get(ebs)[y << 8 | z << 4 | x] = (short) id;
    }

    public static short[] create16BArray() {
        return new short[Constants.ebsIdArrayLength];
    }

    public static short[] get(ExtendedBlockStorage ebs) {
        return null; // populated via asm (SelfHooks)
    }

    public static void setTickRefCount(ExtendedBlockStorage ebs, int value) {
        // populated via asm (SelfHooks)
    }

    public static void setBlockRefCount(ExtendedBlockStorage ebs, int value) {
        // populated via asm (SelfHooks)
    }

    public static void removeInvalidBlocksHook(ExtendedBlockStorage ebs) {
        short[] blkIds = get(ebs);
        int cntNonEmpty = 0;
        int cntTicking = 0;

        for (int off = 0; off < blkIds.length; off++) {
            int id = blkIds[off] & 0xffff;

            if (id > 0) {
                Block block = (Block) Block.blockRegistry.getObjectById(id);

                if (block == null) {
                    if (IEConfig.removeInvalidBlocks)
                        blkIds[off] = 0; // NOTE: setting the block to 0 is not vanilla behavior

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

    public static int getIdFromBlockWithCheck(Block block, Block oldBlock) {
        int id = Block.getIdFromBlock(block);

        if (IEConfig.catchUnregisteredBlocks && id == -1) {
            throw new IllegalArgumentException(
                    "Block " + block
                            + " is not registered. <-- Say about this to the author of this mod, or you can try to enable \"RemoveInvalidBlocks\" option in NEID config.");
        }

        if (id < 0 || id > Constants.MAX_BLOCK_ID) {
            if (id == -1) { // If Block is not registered
                return Block.getIdFromBlock(oldBlock);
            }
            throw new IllegalArgumentException("id out of range: " + id);
        }

        return id;
    }
}
