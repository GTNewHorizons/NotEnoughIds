package com.gtnewhorizons.neid.mixins.early.minecraft;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

/**
 * This mixin exists to completely re-work the way that block harvestability is calculated. Forge adds this system
 * on-top of vanilla, which creates a pre-populated array on each block with the size of the maximum metadata value. For
 * instance, in vanilla we get a String array, and an int array both of size 16, pre-populated with null and -1,
 * respectively. This is awful for scalability with adding more possible Blocks, and increasing the metadata to 16-bits
 * from 4. This uproots the under-workings of that system to use a default and dynamic HashMaps for metadata specific
 * values, so we only use the memory we need, because most things don't even take advantage of this system, especially
 * based on block metadatas. The public methods of this system maintain API compatibility, so anything using those
 * should still function perfectly the same. The only potential breakage is if something were to reflect/ASM/mixin with
 * the private values/methods we're overwriting.
 */
@Mixin(Block.class)
public class MixinBlock {

    @Unique
    private String neid$defaultHarvestTool = null;

    @Unique
    private int neid$defaultHarvestLevel = -1;

    @Unique
    private Int2ObjectOpenHashMap<String> harvestToolMap = new Int2ObjectOpenHashMap<>();

    @Unique
    private Int2IntOpenHashMap harvestLevelMap = new Int2IntOpenHashMap();

    @Shadow(remap = false)
    private String[] harvestTool = null;

    @Shadow(remap = false)
    private int[] harvestLevel = null;

    /**
     * @author Cleptomania
     * @reason Support 16-bit metadata without using GBs of memory. Classdump of full GTNH shows nothing else
     *         ASMing/mixin to this.
     */
    @Overwrite(remap = false)
    public void setHarvestLevel(String toolClass, int level) {
        this.neid$defaultHarvestTool = toolClass;
        this.neid$defaultHarvestLevel = level;
    }

    /**
     * @author Cleptomania
     * @reason Support 16-bit metadata without using GBs of memory. Classdump of full GTNH shows nothing else
     *         ASMing/mixin to this.
     */
    @Overwrite(remap = false)
    public void setHarvestLevel(String toolClass, int level, int metadata) {
        this.harvestToolMap.put(metadata, toolClass);
        this.harvestLevelMap.put(metadata, level);
    }

    /**
     * @author Cleptomania
     * @reason Support 16-bit metadata without using GBs of memory. Classdump of full GTNH shows nothing else
     *         ASMing/mixin to this.
     */
    @Overwrite(remap = false)
    public String getHarvestTool(int metadata) {
        return this.harvestToolMap.getOrDefault(metadata, this.neid$defaultHarvestTool);
    }

    /**
     * @author Cleptmania
     * @reason Support 16-bit metadata without using GBs of memory. Classdump of full GTNH shows nothing else
     *         ASMing/mixin to this.
     */
    @Overwrite(remap = false)
    public int getHarvestLevel(int metadata) {
        return this.harvestLevelMap.getOrDefault(metadata, this.neid$defaultHarvestLevel);
    }

    /**
     * @author Cleptomania
     * @reason Support 16-bit metadata without using GBs of memory. Classdump of full GTNH shows nothing else
     *         ASMing/mixin to this.
     */
    @Overwrite(remap = false)
    public boolean isToolEffective(String type, int metadata) {
        Block self = ((Block) (Object) this);
        if ("pickaxe".equals(type)
                && (self == Blocks.redstone_ore || self == Blocks.lit_redstone_ore || self == Blocks.obsidian))
            return false;
        String harvestTool = this.getHarvestTool(metadata);
        if (harvestTool == null) return false;
        return harvestTool.equals(type);
    }

}
