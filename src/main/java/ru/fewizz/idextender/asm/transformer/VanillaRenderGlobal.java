package ru.fewizz.idextender.asm.transformer;

import org.objectweb.asm.tree.*;
import ru.fewizz.idextender.asm.*;

public class VanillaRenderGlobal implements IClassNodeTransformer {
    @Override
    public void transform(final ClassNode cn, final boolean obfuscated) {
        final MethodNode method = AsmUtil.findMethod(cn, Name.renderGlobal_playAuxSFX);
        AsmUtil.transformInlinedSizeMethod(cn, method, 4095, 65535);
        AsmUtil.transformInlinedSizeMethod(cn, method, 12, 16);
    }
}
