package com.gtnewhorizons.neid.mixins.interfaces;

import java.nio.ShortBuffer;
import java.util.function.IntToLongFunction;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

import com.gtnewhorizon.gtnhlib.util.CoordinatePacker;
import com.gtnewhorizon.gtnhlib.util.data.BlockMeta;

/**
 * An object that can transform a block before it is sent to the client. This is useful for situations where the client
 * must see a non-TE block without mutating the server's world (i.e. in rendering where the client should do something
 * special with a block).
 */
public interface ClientBlockTransformer {

    /**
     * Transforms a single block.
     * 
     * @param world     The world
     * @param x         The world-space x coord
     * @param y         The world-space y coord
     * @param z         The world-space z coord
     * @param blockMeta The existing block/meta. Mutate this object to change the resulting block.
     * @return True to change the block, false to do nothing
     */
    boolean transformBlock(World world, int x, int y, int z, BlockMeta blockMeta);

    /**
     * Transforms a buffer of block ids + metas. The block and meta buffers are guaranteed to be the same length.
     * 
     * @param world  The world
     * @param coord  A function which takes an index into the buffer and transforms it into a packed x,y,z coordinate.
     * @param blocks The buffer of block ids.
     * @param metas  The buffer of meta values.
     */
    default void transformBulk(World world, IntToLongFunction coord, ShortBuffer blocks, ShortBuffer metas) {
        BlockIdCache input = new BlockIdCache();
        BlockIdCache output = new BlockIdCache();

        int len = blocks.capacity();

        BlockMeta pooled = new BlockMeta(Blocks.air, 0);

        for (int i = 0; i < len; i++) {
            pooled.setBlock(input.getBlock(blocks.get(i)));
            pooled.setBlockMeta(metas.get(i));

            long c = coord.applyAsLong(i);

            boolean didSomething = transformBlock(
                    world,
                    CoordinatePacker.unpackX(c),
                    CoordinatePacker.unpackY(c),
                    CoordinatePacker.unpackZ(c),
                    pooled);

            if (didSomething) {
                blocks.put(i, (short) output.getId(pooled.getBlock()));
                metas.put(i, (short) pooled.getBlockMeta());
            }
        }
    }

    class BlockIdCache {

        public Block block;
        public int blockId = -1;

        public int getId(Block block) {
            if (this.block == block) {
                return blockId;
            }

            this.block = block;
            this.blockId = Block.getIdFromBlock(block);

            return blockId;
        }

        public Block getBlock(int id) {
            if (id == blockId) {
                return block;
            }

            this.blockId = id;
            this.block = Block.getBlockById(id);

            return block;
        }
    }
}
