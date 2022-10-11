package ru.fewizz.idextender.asm.transformer;

import ru.fewizz.idextender.asm.*;
import org.objectweb.asm.tree.*;
import java.util.*;

public class FmlRegistry implements IClassNodeTransformer
{
    @Override
    public void transform(final ClassNode cn, final boolean obfuscated) {
        final FieldNode field = AsmUtil.findField(cn, "MAX_BLOCK_ID", true);
        if (field != null) {
            field.value = 32767;
        }
        boolean found = false;
        for (final MethodNode method : cn.methods) {
            if (AsmUtil.transformInlinedSizeMethod(cn, method, 4095, 32767, true)) {
                found = true;
            }
        }
        if (!found) {
            throw new AsmTransformException("can't find 4095 constant in any method");
        }
    }
}
