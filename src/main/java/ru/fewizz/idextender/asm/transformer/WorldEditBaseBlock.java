package ru.fewizz.idextender.asm.transformer;

import ru.fewizz.idextender.asm.*;
import org.objectweb.asm.tree.*;
import java.util.*;

public class WorldEditBaseBlock implements IClassNodeTransformer
{
    @Override
    public void transform(final ClassNode cn, final boolean obfuscated) {
        final MethodNode method = AsmUtil.findMethod(cn, "internalSetId", true);
        if (method == null) {
            return;
        }
        AsmUtil.transformInlinedSizeMethod(cn, method, 4095, 32767);
        final InsnList code = method.instructions;
        for (final AbstractInsnNode insn : code) {
            if (insn.getType() == 9 && ((LdcInsnNode)insn).cst instanceof String) {
                final String string = (String)((LdcInsnNode)insn).cst;
                if (string.contains("4095")) {
                    ((LdcInsnNode)insn).cst = string.replace("4095", Integer.toString(32767));
                    break;
                }
                continue;
            }
        }
    }
}
