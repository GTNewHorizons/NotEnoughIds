package ru.fewizz.idextender.asm.transformer;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import ru.fewizz.idextender.asm.AsmUtil;
import ru.fewizz.idextender.asm.IClassNodeTransformer;

public class CofhBlockHelper implements IClassNodeTransformer {

    @Override
    public void transform(final ClassNode cn, final boolean obfuscated) {
        final MethodNode method = AsmUtil.findMethod(cn, "<clinit>");
        if (!AsmUtil.transformInlinedSizeMethod(cn, method, 4096, 32768, true)) {
            AsmUtil.transformInlinedSizeMethod(cn, method, 1024, 32768);
        }
    }
}
