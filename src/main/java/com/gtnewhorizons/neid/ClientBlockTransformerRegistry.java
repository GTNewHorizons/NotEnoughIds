package com.gtnewhorizons.neid;

import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.IntToLongFunction;

import net.minecraft.world.World;

import com.gtnewhorizon.gtnhlib.util.data.BlockMeta;
import com.gtnewhorizons.neid.mixins.interfaces.ClientBlockTransformer;

public class ClientBlockTransformerRegistry {

    private static final List<ClientBlockTransformer> TRANSFORMERS = new ArrayList<>();

    public static void registerTransformer(ClientBlockTransformer transformer) {
        TRANSFORMERS.add(transformer);
    }

    public static boolean transformBlock(World world, int x, int y, int z, BlockMeta blockMeta) {
        int size = TRANSFORMERS.size();

        boolean didSomething = false;

        for (int i = 0; i < size; i++) {
            didSomething |= TRANSFORMERS.get(i).transformBlock(world, x, y, z, blockMeta);
        }

        return didSomething;
    }

    public static void transformBulk(World world, IntToLongFunction coord, ShortBuffer blocks, ShortBuffer metas) {
        int size = TRANSFORMERS.size();

        for (int i = 0; i < size; i++) {
            TRANSFORMERS.get(i).transformBulk(world, coord, blocks, metas);
        }
    }
}
