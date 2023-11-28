package com.gtnewhorizons.neid.asm.transformer;

import java.util.ListIterator;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.gtnewhorizons.neid.asm.AsmUtil;
import com.gtnewhorizons.neid.asm.IClassNodeTransformer;
import com.gtnewhorizons.neid.asm.Name;

public class UndergroundBiomesBiomeUndergroundDecorator implements IClassNodeTransformer {

    @Override
    public String[] getTargetClass() {
        return new String[] { "exterminatorJeff.undergroundBiomes.worldGen.BiomeUndergroundDecorator" };
    }

    @Override
    public void transform(final ClassNode cn, final boolean obfuscated) {
        MethodNode method = AsmUtil.findMethod(cn, Name.ub_bud_replaceChunkOres_world);
        this.transformReplaceChunkOres(method, obfuscated, 1);
        method = AsmUtil.findMethod(cn, Name.ub_bud_replaceChunkOres_iChunkProvider);
        this.transformReplaceChunkOres(method, obfuscated, 0);
    }

    private void transformReplaceChunkOres(final MethodNode method, final boolean obfuscated, final int varOffset) {
        final InsnList code = method.instructions;
        final ListIterator<AbstractInsnNode> iterator = code.iterator();
        while (iterator.hasNext()) {
            final AbstractInsnNode insn = iterator.next();
            if (insn.getOpcode() == INVOKEVIRTUAL
                    && Name.ebs_getBlockLSBArray.matches((MethodInsnNode) insn, obfuscated)) {
                iterator.remove();
                iterator.add(new VarInsnNode(ILOAD, 9 + varOffset));
                iterator.add(new VarInsnNode(ILOAD, 18 + varOffset));
                iterator.add(new VarInsnNode(ILOAD, 10 + varOffset));
                iterator.add(Name.hooks_getBlockId.staticInvocation(obfuscated));
                while (iterator.next().getOpcode() != ISTORE) {
                    iterator.remove();
                }
                break;
            }
        }
    }
}
