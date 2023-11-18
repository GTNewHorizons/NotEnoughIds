package com.gtnewhorizons.neid.asm.transformer;

import java.util.ListIterator;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.gtnewhorizons.neid.asm.AsmTransformException;
import com.gtnewhorizons.neid.asm.AsmUtil;
import com.gtnewhorizons.neid.asm.IClassNodeTransformer;
import com.gtnewhorizons.neid.asm.Name;

public class VanillaChunk implements IClassNodeTransformer {

    @Override
    public void transform(final ClassNode cn, final boolean obfuscated) {
        final MethodNode method = AsmUtil.findMethod(cn, Name.chunk_fillChunk, true);
        if (method == null) {
            return;
        }
        method.localVariables = null;
        int part = 0;
        final ListIterator<AbstractInsnNode> it = method.instructions.iterator();
        while (it.hasNext()) {
            AbstractInsnNode insn = it.next();
            if (part == 0) {
                if (insn.getOpcode() != 182 || !Name.ebs_getBlockLSBArray.matches((MethodInsnNode) insn, obfuscated)) {
                    continue;
                }
                it.set(new VarInsnNode(25, 1));
                it.add(new VarInsnNode(21, 6));
                it.add(Name.hooks_setBlockData.staticInvocation(obfuscated));
                ++part;
            } else if (part == 1) {
                if (insn.getOpcode() == 96) {
                    it.set(new VarInsnNode(21, 6));
                    it.add(new LdcInsnNode(8192));
                    it.add(new InsnNode(96));
                    ++part;
                } else {
                    it.remove();
                }
            } else {
                if (part == 2 && insn.getOpcode() == 182
                        && Name.ebs_getBlockMSBArray.matches((MethodInsnNode) insn, obfuscated)) {
                    while (it.previous().getOpcode() != 3) {}
                    while (it.previous().getType() != 8) {}
                    do {
                        insn = it.next();
                    } while (insn.getOpcode() != 161 && insn.getOpcode() != 162);
                    it.set(new JumpInsnNode((insn.getOpcode() == 161) ? 163 : 161, ((JumpInsnNode) insn).label));
                    return;
                }
                continue;
            }
        }
        throw new AsmTransformException("no match for part " + part);
    }
}
