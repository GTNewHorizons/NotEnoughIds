package ru.fewizz.idextender.asm.transformer;

import ru.fewizz.idextender.asm.*;
import org.objectweb.asm.tree.*;

public class CofhBlockHelper implements IClassNodeTransformer
{
    @Override
    public void transform(final ClassNode cn, final boolean obfuscated) {
        final MethodNode method = AsmUtil.findMethod(cn, "<clinit>");
        if (!AsmUtil.transformInlinedSizeMethod(cn, method, 4096, 32768, true)) {
            AsmUtil.transformInlinedSizeMethod(cn, method, 1024, 32768);
        }
    }
}
