package com.gtnewhorizons.neid.mixins.early.biome.minecraft;

import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.util.Arrays;

import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.Chunk;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.gtnewhorizons.neid.mixins.interfaces.IChunkMixin;
import com.llamalad7.mixinextras.injector.WrapWithCondition;

@Mixin(Chunk.class)
public class MixinChunk implements IChunkMixin {

    @Shadow
    @Final
    public int xPosition;

    @Shadow
    @Final
    public int zPosition;

    @Shadow
    private byte[] blockBiomeArray;

    @Unique
    private short[] block16BBiomeArray;

    @Shadow
    public World worldObj;

    @Inject(method = "<init>(Lnet/minecraft/world/World;II)V", at = @At(value = "RETURN"), require = 1)
    private void neid$replaceBlockBiomArray(World world, int i1, int i2, CallbackInfo ci) {
        blockBiomeArray = null;
        block16BBiomeArray = new short[256];
        Arrays.fill(block16BBiomeArray, (short) -1);
    }

    @WrapWithCondition(
            method = "<init>(Lnet/minecraft/world/World;II)V",
            at = @At(value = "INVOKE", target = "Ljava/util/Arrays;fill([BB)V"),
            require = 1)
    private boolean neid$cancelOriginalArrayFill(byte[] oldBytes, byte theSet) {
        return false;
    }

    @Inject(method = "getBiomeArray", at = @At(value = "HEAD"), require = 1)
    private void neid$panic$getBiomeArray(CallbackInfoReturnable<Boolean> ci) {
        throw new UnsupportedOperationException("A mod tried to use getBiomeArray with extended biome IDs enabled.");
    }

    @Inject(method = "setBiomeArray", at = @At(value = "HEAD"), require = 1)
    private void neid$panic$setBiomeArray(CallbackInfo ci) {
        throw new UnsupportedOperationException("A mod tried to use setBiomeArray with extended biome IDs enabled.");
    }

    /**
     * @author Cleptomania
     * @reason Hard to fix with mixin, added in patch from ArchaicFix
     */
    @Overwrite
    public BiomeGenBase getBiomeGenForWorldCoords(int x, int z, WorldChunkManager manager) {
        int id = this.block16BBiomeArray[z << 4 | x] & 65535;
        if (id == 65535) {
            // Source:
            // https://github.com/embeddedt/ArchaicFix/blob/3d1392f4db5a7221534a6f9b00c0c36a49d9be59/src/main/java/org/embeddedt/archaicfix/mixins/common/core/MixinChunk.java#L52
            if (this.worldObj.isRemote) {
                return BiomeGenBase.ocean;
            }
            BiomeGenBase gen = manager.getBiomeGenAt((this.xPosition << 4) + x, (this.zPosition << 4) + z);
            id = gen.biomeID;
            this.block16BBiomeArray[z << 4 | x] = (short) (id & 65535);
        }

        return BiomeGenBase.getBiome(id) == null ? BiomeGenBase.plains : BiomeGenBase.getBiome(id);
    }

    @Override
    public short[] getBiome16BArray() {
        return block16BBiomeArray;
    }

    @Override
    public void setBiome16BArray(short[] biomeArray) {
        this.block16BBiomeArray = biomeArray;
    }

    @Override
    public byte[] getBiomeData() {
        final byte[] ret = new byte[this.block16BBiomeArray.length * 2];
        ByteBuffer.wrap(ret).asShortBuffer().put(this.block16BBiomeArray);
        return ret;
    }

    @Override
    public void setBiomeData(byte[] data, int offset) {
        ShortBuffer.wrap(this.block16BBiomeArray).put(ByteBuffer.wrap(data, offset, 256).asShortBuffer());
    }
}
