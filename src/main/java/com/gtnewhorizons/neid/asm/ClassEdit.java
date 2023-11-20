package com.gtnewhorizons.neid.asm;

import java.util.HashMap;
import java.util.Map;

import com.gtnewhorizons.neid.asm.transformer.CofhBlockHelper;
import com.gtnewhorizons.neid.asm.transformer.FmlRegistry;
import com.gtnewhorizons.neid.asm.transformer.MFQM;
import com.gtnewhorizons.neid.asm.transformer.UndergroundBiomesBiomeUndergroundDecorator;
import com.gtnewhorizons.neid.asm.transformer.UndergroundBiomesOreUBifier;
import com.gtnewhorizons.neid.asm.transformer.VanillaDataWatcher;
import com.gtnewhorizons.neid.asm.transformer.WorldEditBaseBlock;

public enum ClassEdit {

    VanillaDataWatcher(new VanillaDataWatcher(), new String[] { "net.minecraft.entity.DataWatcher" }),
    FmlRegistry(new FmlRegistry(),
            new String[] { "cpw.mods.fml.common.registry.GameData",
                    "cpw.mods.fml.common.registry.FMLControlledNamespacedRegistry" }),
    CofhBlockHelper(new CofhBlockHelper(), new String[] { "cofh.lib.util.helpers.BlockHelper" }),
    UndergroundBiomesOreUBifier(new UndergroundBiomesOreUBifier(),
            new String[] { "exterminatorJeff.undergroundBiomes.worldGen.OreUBifier" }),
    UndergroundBiomesBiomeUndergroundDecorator(new UndergroundBiomesBiomeUndergroundDecorator(),
            new String[] { "exterminatorJeff.undergroundBiomes.worldGen.BiomeUndergroundDecorator" }),
    MFQM(new MFQM(), new String[] { "MoreFunQuicksandMod.main.MFQM" }),
    WorldEditBaseBlock(new WorldEditBaseBlock(), new String[] { "com.sk89q.worldedit.blocks.BaseBlock" });

    private static final Map<String, ClassEdit> editMap;
    private final IClassNodeTransformer transformer;
    private final String[] classNames;

    ClassEdit(final IClassNodeTransformer transformer, final String[] classNames) {
        this.transformer = transformer;
        this.classNames = classNames;
    }

    public static ClassEdit get(final String className) {
        return ClassEdit.editMap.get(className);
    }

    public String getName() {
        return this.name();
    }

    public IClassNodeTransformer getTransformer() {
        return this.transformer;
    }

    static {
        editMap = new HashMap<String, ClassEdit>();
        for (final ClassEdit edit : values()) {
            for (final String name : edit.classNames) {
                ClassEdit.editMap.put(name, edit);
            }
        }
    }
}
