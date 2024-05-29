package com.gtnewhorizons.neid.mixins.late.biome.thaumcraft;

import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import com.gtnewhorizons.neid.mixins.interfaces.IChunkMixin;

import cpw.mods.fml.common.network.NetworkRegistry;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.misc.PacketBiomeChange;
import thaumcraft.common.lib.utils.Utils;

/**
 * This mixin is not Pseudo like the others because: 1. I needed to Overwrite because couldn't find any other good way
 * to do it 2. The code inside needs other Thaumcraft code.
 */
@Mixin(value = Utils.class, remap = false)
public class MixinUtils {

    /**
     * @author Cleptomania
     * @reason For biome extension, no good way to do with other means, doesn't seem to cause problem in GTNH
     */
    @Overwrite
    public static void setBiomeAt(World world, int x, int z, BiomeGenBase biome) {
        if (biome == null) return;
        Chunk chunk = world.getChunkFromBlockCoords(x, z);
        short[] array = ((IChunkMixin) chunk).getBiome16BArray();
        array[(z & 0xF) << 4 | x & 0xF] = (short) (biome.biomeID);
        // Thaumcraft has a call to setBiomeArray here, but that's unnecessary, thanks references
        if (!world.isRemote) {
            PacketHandler.INSTANCE.sendToAllAround(
                    new PacketBiomeChange(x, z, (short) biome.biomeID),
                    new NetworkRegistry.TargetPoint(
                            world.provider.dimensionId,
                            x,
                            world.getHeightValue(x, z),
                            z,
                            32.0));
        }
    }
}
