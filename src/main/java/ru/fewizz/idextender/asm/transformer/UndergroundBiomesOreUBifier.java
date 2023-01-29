package ru.fewizz.idextender.asm.transformer;

import org.objectweb.asm.tree.*;

import ru.fewizz.idextender.asm.*;

public class UndergroundBiomesOreUBifier implements IClassNodeTransformer {

    @Override
    public void transform(final ClassNode cn, final boolean obfuscated) {
        final MethodNode method = AsmUtil.findMethod(cn, "renewBlockReplacers");
        AsmUtil.transformInlinedSizeMethod(cn, method, 4096, 32768, false);
    }
}
