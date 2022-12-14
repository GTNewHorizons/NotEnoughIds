package ru.fewizz.idextender.asm.transformer;

import java.util.*;
import org.objectweb.asm.tree.*;
import ru.fewizz.idextender.asm.*;

public class UndergroundBiomesBiomeUndergroundDecorator implements IClassNodeTransformer {
    @Override
    public void transform(final ClassNode cn, final boolean obfuscated) {
        MethodNode method = AsmUtil.findMethod(cn, Name.ub_bud_replaceChunkOres_world);
        this.transformReplaceChunkOres(cn, method, obfuscated, 1);
        method = AsmUtil.findMethod(cn, Name.ub_bud_replaceChunkOres_iChunkProvider);
        this.transformReplaceChunkOres(cn, method, obfuscated, 0);
    }

    private void transformReplaceChunkOres(
            final ClassNode cn, final MethodNode method, final boolean obfuscated, final int varOffset) {
        final InsnList code = method.instructions;
        final ListIterator<AbstractInsnNode> iterator = (ListIterator<AbstractInsnNode>) code.iterator();
        while (iterator.hasNext()) {
            final AbstractInsnNode insn = iterator.next();
            if (insn.getOpcode() == 182 && Name.ebs_getBlockLSBArray.matches((MethodInsnNode) insn, obfuscated)) {
                iterator.remove();
                iterator.add((AbstractInsnNode) new VarInsnNode(21, 9 + varOffset));
                iterator.add((AbstractInsnNode) new VarInsnNode(21, 18 + varOffset));
                iterator.add((AbstractInsnNode) new VarInsnNode(21, 10 + varOffset));
                iterator.add((AbstractInsnNode) Name.hooks_getBlockId.staticInvocation(obfuscated));
                while (iterator.next().getOpcode() != 54) {
                    iterator.remove();
                }
                break;
            }
        }
    }
}
