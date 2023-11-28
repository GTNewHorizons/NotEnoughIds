package com.gtnewhorizons.neid.asm;

import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.tree.ClassNode;

import com.gtnewhorizons.neid.asm.transformer.CofhBlockHelper;
import com.gtnewhorizons.neid.asm.transformer.FmlRegistry;
import com.gtnewhorizons.neid.asm.transformer.MFQM;
import com.gtnewhorizons.neid.asm.transformer.UndergroundBiomesBiomeUndergroundDecorator;
import com.gtnewhorizons.neid.asm.transformer.UndergroundBiomesOreUBifier;
import com.gtnewhorizons.neid.asm.transformer.WorldEditBaseBlock;

public enum ClassNodeTransformers {

    CofhBlockHelper(new CofhBlockHelper()),
    FmlRegistry(new FmlRegistry()),
    MFQM(new MFQM()),
    UndergroundBiomesBiomeUndergroundDecorator(new UndergroundBiomesBiomeUndergroundDecorator()),
    UndergroundBiomesOreUBifier(new UndergroundBiomesOreUBifier()),
    WorldEditBaseBlock(new WorldEditBaseBlock());

    private static final Map<String, ClassNodeTransformers> transformers = new HashMap<>();
    private final IClassNodeTransformer transformer;

    static {
        for (final ClassNodeTransformers enumEntry : values()) {
            for (final String className : enumEntry.transformer.getTargetClass()) {
                ClassNodeTransformers.transformers.put(className, enumEntry);
            }
        }
    }

    public static ClassNodeTransformers get(final String className) {
        return ClassNodeTransformers.transformers.get(className);
    }

    ClassNodeTransformers(final IClassNodeTransformer transformer) {
        this.transformer = transformer;
    }

    public String getName() {
        return this.name();
    }

    public void transform(ClassNode cn, boolean obfuscated) {
        this.transformer.transform(cn, obfuscated);
    }
}
