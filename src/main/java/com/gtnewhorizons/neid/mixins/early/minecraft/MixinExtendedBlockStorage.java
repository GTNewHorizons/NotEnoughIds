package com.gtnewhorizons.neid.mixins.early.minecraft;

import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import com.gtnewhorizons.neid.Constants;
import com.gtnewhorizons.neid.NEIDConfig;
import com.gtnewhorizons.neid.mixins.interfaces.IExtendedBlockStorageMixin;

@Mixin(ExtendedBlockStorage.class)
public class MixinExtendedBlockStorage implements IExtendedBlockStorageMixin {

    @Shadow
    private int blockRefCount;

    @Shadow
    private int tickRefCount;

    private short[] block16BArray = new short[Constants.BLOCKS_PER_EBS];

    @Override
    public short[] getBlock16BArray() {
        return this.block16BArray;
    }

    @Override
    public byte[] getBlockData() {
        final byte[] ret = new byte[this.block16BArray.length * 2];
        ByteBuffer.wrap(ret).asShortBuffer().put(this.block16BArray);
        return ret;
    }

    @Override
    public void setBlockData(byte[] data, int offset) {
        ShortBuffer.wrap(this.block16BArray)
                .put(ByteBuffer.wrap(data, offset, Constants.BLOCKS_PER_EBS * 2).asShortBuffer());
    }

    private int getBlockId(int x, int y, int z) {
        return block16BArray[y << 8 | z << 4 | x] & 0xFFFF;
    }

    private void setBlockId(int x, int y, int z, int id) {
        block16BArray[y << 8 | z << 4 | x] = (short) id;
    }

    /**
     * @author Cleptomania
     * @reason Shims our block16BArray short array in place of built-in blockLSBArray. Original ASM was a complete
     *         overwrite as well. Likely no collision here.
     */
    @Overwrite
    public Block getBlockByExtId(int x, int y, int z) {
        return Block.getBlockById(getBlockId(x, y, z));
    }

    /**
     * @author Cleptomania
     * @reason This is for setExtBlockID but the function isn't deobf'd. Original ASM was not a complete overwrite, but
     *         was pretty close to it Extreme doubt that anything would conflict with this one.
     */
    @Overwrite
    public void func_150818_a(int x, int y, int z, Block b) {
        Block old = this.getBlockByExtId(x, y, z);
        if (old != Blocks.air) {
            --this.blockRefCount;
            if (old.getTickRandomly()) {
                --this.tickRefCount;
            }
        }

        if (b != Blocks.air) {
            ++this.blockRefCount;
            if (b.getTickRandomly()) {
                ++this.tickRefCount;
            }
        }

        int newId = Block.getIdFromBlock(b);
        if (NEIDConfig.catchUnregisteredBlocks && newId == -1) {
            throw new IllegalArgumentException(
                    "Block " + b
                            + " is not registered. <-- Say about this to the author of this mod, or you can try to enable \"RemoveInvalidBlocks\" option in NEID config.");
        }
        if ((newId < 0 || newId > Constants.MAX_BLOCK_ID) && (newId != -1)) {
            throw new IllegalArgumentException("id out of range: " + newId);
        }
        if (newId == -1) {
            newId = Block.getIdFromBlock(old);
        }

        this.setBlockId(x, y, z, newId);
    }

    /**
     * @author Cleptomania
     * @reason Original ASM was a complete overwrite to redirect to Hooks.removeInvalidBlocksHook which accepted the
     *         ExtendedBlockStorage class as a parameter. That method has been re-implemented here and modified to use
     *         the new block16BArray provided by the mixin, as opposed to getting the data from ExtendedBlockStorage.
     */
    @Overwrite
    public void removeInvalidBlocks() {
        for (int off = 0; off < block16BArray.length; ++off) {
            final int id = block16BArray[off] & 0xFFFF;
            if (id > 0) {
                final Block block = (Block) Block.blockRegistry.getObjectById(id);
                if (block == null) {
                    if (NEIDConfig.removeInvalidBlocks) {
                        block16BArray[off] = 0;
                    }
                } else if (block != Blocks.air) {
                    ++blockRefCount;
                    if (block.getTickRandomly()) {
                        ++tickRefCount;
                    }
                }
            }
        }
    }

}
