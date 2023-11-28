package com.gtnewhorizons.neid.mixins.early.minecraft;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

/**
 * The mixins within here exist to override the way vanilla Minecraft handles harvest levels for blocks. Vanilla by
 * default creates two arrays, with a length of 16, with the index being the metadata. These arrays get populated with
 * default values to exist for every possible metadata value, regardless of if a block even has multiple metadata types.
 * To use this same system with 16-bit metadata values, means taking several GBs of RAM for just that storage on the
 * blocks. This instead creates a default value(the same defaults as normal vanilla, just not populated for every
 * metadata value) and then creates a hashmap for lookups which gets populated as needed via setHarvestLevel calls. If a
 * value is not present for the requested metadata in the map, then the default will be returned instead.
 */
@Mixin(Block.class)
public class MixinBlock {

    private String defaultHarvestTool = null;
    private int defaultHarvestLevel = -1;
    private Map<Integer, String> harvestToolMap = new HashMap<Integer, String>();
    private Map<Integer, Integer> harvestLevelMap = new HashMap<Integer, Integer>();

    @Shadow(remap = false)
    private String[] harvestTool = null;

    @Shadow(remap = false)
    private int[] harvestLevel = null;

    /**
     * @author Cleptomania
     * @reason Support 16-bit metadata without using GBs of memory
     */
    @Overwrite(remap = false)
    public void setHarvestLevel(String toolClass, int level) {
        this.defaultHarvestTool = toolClass;
        this.defaultHarvestLevel = level;
    }

    /**
     * @author Cleptomania
     * @reason Support 16-bit metadata without using GBs of memory
     */
    @Overwrite(remap = false)
    public void setHarvestLevel(String toolClass, int level, int metadata) {
        this.harvestToolMap.put(metadata, toolClass);
        this.harvestLevelMap.put(metadata, level);
    }

    /**
     * @author Cleptomania
     * @reason Support 16-bit metadata without using GBs of memory
     */
    @Overwrite(remap = false)
    public String getHarvestTool(int metadata) {
        return this.harvestToolMap.getOrDefault(metadata, this.defaultHarvestTool);
    }

    /**
     * @author Cleptomania
     * @reason Support 16-bit metadata without using GBs of memory
     */
    @Overwrite(remap = false)
    public int getHarvestLevel(int metadata) {
        return this.harvestLevelMap.getOrDefault(metadata, this.defaultHarvestLevel);
    }

    /**
     * @author Cleptomania
     * @reason Support 16-bit metadata without using GBs of memory
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
