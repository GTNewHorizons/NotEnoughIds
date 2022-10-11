package ru.fewizz.idextender.asm.transformer;

import ru.fewizz.idextender.asm.*;
import org.objectweb.asm.tree.*;
import java.util.*;

public class VanillaS22PacketMultiBlockChange implements IClassNodeTransformer
{
    @Override
    public void transform(final ClassNode cn, final boolean obfuscated) {
        final MethodNode method = AsmUtil.findMethod(cn, Name.s22_init_server);
        final InsnList code = method.instructions;
        int part = 0;
        final ListIterator<AbstractInsnNode> iterator = (ListIterator<AbstractInsnNode>)code.iterator();
        while (iterator.hasNext()) {
            final AbstractInsnNode insn = iterator.next();
            if (part == 0) {
                if (insn.getOpcode() != 7) {
                    continue;
                }
                iterator.set((AbstractInsnNode)new InsnNode(8));
                ++part;
            }
            else if (part == 1) {
                if (insn.getOpcode() != 184) {
                    continue;
                }
                final MethodInsnNode node = (MethodInsnNode)insn;
                if (!Name.block_getIdFromBlock.matches(node, obfuscated)) {
                    continue;
                }
                iterator.add((AbstractInsnNode)new MethodInsnNode(182, "java/io/DataOutputStream", "writeShort", "(I)V", false));
                ++part;
            }
            else if (part == 2) {
                if (insn.getOpcode() == 25) {
                    ++part;
                }
                else {
                    iterator.remove();
                }
            }
            else if (part == 3) {
                if (insn.getOpcode() != 182) {
                    continue;
                }
                iterator.add((AbstractInsnNode)new VarInsnNode(25, 6));
                iterator.add((AbstractInsnNode)new InsnNode(95));
                iterator.add((AbstractInsnNode)new MethodInsnNode(182, "java/io/DataOutputStream", "writeByte", "(I)V", false));
                ++part;
            }
            else {
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
