package ru.fewizz.idextender.asm.transformer;

import org.objectweb.asm.tree.*;
import ru.fewizz.idextender.asm.*;
import java.util.*;

public class VanillaBlockFire implements IClassNodeTransformer
{
    @Override
    public void transform(final ClassNode cn, final boolean obfuscated) {
        boolean found = false;
        for (final MethodNode method : cn.methods) {
            if (AsmUtil.transformInlinedSizeMethod(cn, method, 4096, 32768, true)) {
                found = true;
            }
        }
        if (!found) {
            throw new AsmTransformException("can't find 4096 constant in any method");
        }
    }
}
