package com.gtnewhorizons.neid.asm.transformer;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import com.gtnewhorizons.neid.asm.AsmUtil;
import com.gtnewhorizons.neid.asm.IClassNodeTransformer;

public class UndergroundBiomesOreUBifier implements IClassNodeTransformer {

    @Override
    public void transform(final ClassNode cn, final boolean obfuscated) {
        final MethodNode method = AsmUtil.findMethod(cn, "renewBlockReplacers");
        AsmUtil.transformInlinedSizeMethod(cn, method, 4096, 32768, false);
    }
}
