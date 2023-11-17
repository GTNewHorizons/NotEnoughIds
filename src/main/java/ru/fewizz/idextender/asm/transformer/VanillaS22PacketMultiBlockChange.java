package ru.fewizz.idextender.asm.transformer;

import java.util.ListIterator;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import ru.fewizz.idextender.asm.AsmTransformException;
import ru.fewizz.idextender.asm.AsmUtil;
import ru.fewizz.idextender.asm.IClassNodeTransformer;
import ru.fewizz.idextender.asm.Name;

public class VanillaS22PacketMultiBlockChange implements IClassNodeTransformer {

    @Override
    public void transform(final ClassNode cn, final boolean obfuscated) {
        final MethodNode method = AsmUtil.findMethod(cn, Name.s22_init_server);
        final InsnList code = method.instructions;
        int part = 0;
        final ListIterator<AbstractInsnNode> iterator = code.iterator();
        while (iterator.hasNext()) {
            final AbstractInsnNode insn = iterator.next();
            if (part == 0) {
                if (insn.getOpcode() != 7) {
                    continue;
                }
                iterator.set(new InsnNode(8));
                ++part;
            } else if (part == 1) {
                if (insn.getOpcode() != 184) {
                    continue;
                }
                final MethodInsnNode node = (MethodInsnNode) insn;
                if (!Name.block_getIdFromBlock.matches(node, obfuscated)) {
                    continue;
                }
                iterator.add(new MethodInsnNode(182, "java/io/DataOutputStream", "writeShort", "(I)V", false));
                ++part;
            } else if (part == 2) {
                if (insn.getOpcode() == 25) {
                    ++part;
                } else {
                    iterator.remove();
                }
            } else if (part == 3) {
                if (insn.getOpcode() != 182) {
                    continue;
                }
                iterator.add(new VarInsnNode(25, 6));
                iterator.add(new InsnNode(95));
                iterator.add(new MethodInsnNode(182, "java/io/DataOutputStream", "writeByte", "(I)V", false));
                ++part;
            } else {
                iterator.remove();
                if (insn.getOpcode() == 182) {
                    return;
                }
                continue;
            }
        }
        throw new AsmTransformException("no match for part " + part);
    }
}
