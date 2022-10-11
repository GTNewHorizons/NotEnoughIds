package ru.fewizz.idextender.asm.transformer;

import ru.fewizz.idextender.asm.*;
import org.objectweb.asm.tree.*;

public class VanillaWorld implements IClassNodeTransformer
{
    @Override
    public void transform(final ClassNode cn, final boolean obfuscated) {
        final MethodNode method = AsmUtil.findMethod(cn, Name.world_breakBlock);
        AsmUtil.transformInlinedSizeMethod(cn, method, 12, 16);
    }
}
