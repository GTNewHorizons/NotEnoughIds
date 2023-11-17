package ru.fewizz.idextender.asm.transformer;

import java.util.ListIterator;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import ru.fewizz.idextender.asm.AsmTransformException;
import ru.fewizz.idextender.asm.AsmUtil;
import ru.fewizz.idextender.asm.IClassNodeTransformer;
import ru.fewizz.idextender.asm.Name;

public class VanillaS21PacketChunkData implements IClassNodeTransformer {

    @Override
    public void transform(final ClassNode cn, final boolean obfuscated) {
        MethodNode method = AsmUtil.findMethod(cn, "<clinit>");
        AsmUtil.transformInlinedSizeMethod(cn, method, 196864, 229632, false);
        method = AsmUtil.findMethod(cn, Name.s21_undefined1);
        AsmUtil.transformInlinedSizeMethod(cn, method, 196864, 229632, false);
        method = AsmUtil.findMethod(cn, Name.packet_readPacketData);
        AsmUtil.transformInlinedSizeMethod(cn, method, 12288, 14336, false);
        method = AsmUtil.findMethod(cn, Name.s21_undefined2);
        this.transformCreateData(cn, method, obfuscated);
    }

    private void transformCreateData(final ClassNode cn, final MethodNode method, final boolean obfuscated) {
        final InsnList code = method.instructions;
        final ListIterator<AbstractInsnNode> iterator = code.iterator();
        while (iterator.hasNext()) {
            final AbstractInsnNode insn = iterator.next();
            if (insn.getOpcode() == 182) {
                final MethodInsnNode node = (MethodInsnNode) insn;
                if (Name.ebs_getBlockLSBArray.matches(node, obfuscated)) {
                    iterator.set(Name.hooks_getBlockData.staticInvocation(obfuscated));
                    return;
                }
                continue;
            }
        }
        throw new AsmTransformException("can't find getBlockLSBArray INVOKEVIRTUAL");
    }
}
