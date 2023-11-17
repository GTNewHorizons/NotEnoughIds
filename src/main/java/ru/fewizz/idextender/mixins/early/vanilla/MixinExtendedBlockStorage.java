package ru.fewizz.idextender.mixins.early.vanilla;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import ru.fewizz.idextender.Hooks;
import ru.fewizz.idextender.IEConfig;

@Mixin(ExtendedBlockStorage.class)
public class MixinExtendedBlockStorage {

    @Shadow
    private int blockRefCount;

    @Shadow
    private int tickRefCount;

    public short[] block16BArray = new short[4096];

    private int getBlockId(int x, int y, int z) {
        return block16BArray[y << 8 | z << 4 | x] & 0xFFFF;
    }

    private void setBlockId(int x, int y, int z, int id) {
        block16BArray[y << 8 | z << 4 | x] = (short) id;
    }

    /**
     * @author Cleptomania
     * @reason
     * Shims our block16BArray short array in place of built-in blockLSBArray. Original ASM was a complete overwrite as
     * well. Likely no collision here.
     */
    @Overwrite
    public Block getBlockByExtId(int x, int y, int z) {
        return Block.getBlockById(getBlockId(x, y, z));
    }

    /**
     * @author Cleptomania
     * @reason
     * This is for setExtBlockID but the function isn't deobf'd. Original ASM was not a complete overwrite, but was
     * pretty close to it Extreme doubt that anything would conflict with this one.
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

        int newId = Hooks.getIdFromBlockWithCheck(b, old);
        this.setBlockId(x, y, z, newId);
    }

    /**
     * @author Cleptomania
     * @reason
     * Original ASM was a complete overwrite to redirect to Hooks.removeInvalidBlocksHook which accepted the
     * ExtendedBlockStorage class as a parameter. That method has been re-implemented here and modified to use the new
     * block16BArray provided by the mixin, as opposed to getting the data from ExtendedBlockStorage.
     */
    @Overwrite
    public void removeInvalidBlocks() {
        for (int off = 0; off < block16BArray.length; ++off) {
            final int id = block16BArray[off] & 0xFFFF;
            if (id > 0) {
                final Block block = (Block) Block.blockRegistry.getObjectById(id);
                if (block == null) {
                    if (IEConfig.removeInvalidBlocks) {
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
