package ru.fewizz.idextender.asm.transformer;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import ru.fewizz.idextender.asm.AsmUtil;
import ru.fewizz.idextender.asm.IClassNodeTransformer;
import ru.fewizz.idextender.asm.Name;

public class VanillaS26PacketMapChunkBulk implements IClassNodeTransformer {
    @Override
    public void transform(ClassNode cn) {
        cn.fields.add(new FieldNode(ACC_PUBLIC, Name.s26_readSize.deobf, "I", null, -1));

        MethodNode method = AsmUtil.findMethod(cn, Name.packet_readPacketData);

        AbstractInsnNode infalte = AsmUtil.findMethodInsnNode(method.instructions.getFirst(), "inflate", null);
        method.instructions.remove(infalte.getNext()); // Remote POP, because now we saving size

        method.instructions.insertBefore(AsmUtil.getRelative(infalte, -2), new VarInsnNode(ALOAD, 0));
        method.instructions.insert(infalte, Name.s26_readSize.putField());

        AbstractInsnNode afterArrayCopy = AsmUtil.findMethodInsnNode(method.instructions.getFirst(), "arraycopy", null)
                .getNext();
        AbstractInsnNode begin = AsmUtil.getRelative(afterArrayCopy, -9);

        AsmUtil.removeRange(method.instructions, begin, afterArrayCopy);

        InsnList il = new InsnList();
        il.add(new VarInsnNode(ALOAD, 3));
        il.add(new VarInsnNode(ALOAD, 0));
        il.add(Name.s26_readSize.getField());
        il.add(new VarInsnNode(ALOAD, 0));
        il.add(Name.s26_sectionsData.getField());
        il.add(new VarInsnNode(ALOAD, 0));
        il.add(Name.s26_sections.getField());
        il.add(new VarInsnNode(ALOAD, 0));
        il.add(Name.s26_hasSky.getField());
        il.add(Name.hooks_grabDataFromChunkBulkPacket.invokeStatic());

        method.instructions.insertBefore(AsmUtil.findInsnNode(afterArrayCopy, RETURN), il);
    }
}
