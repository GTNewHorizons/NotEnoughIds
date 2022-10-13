package ru.fewizz.idextender.asm.transformer;

import java.util.ListIterator;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import ru.fewizz.idextender.asm.AsmTransformException;
import ru.fewizz.idextender.asm.AsmUtil;
import ru.fewizz.idextender.asm.IClassNodeTransformer;
import ru.fewizz.idextender.asm.Name;

public class VanillaAnvilChunkLoader implements IClassNodeTransformer {
    @Override
    public void transform(ClassNode cn) {
        MethodNode method = AsmUtil.findMethod(cn, Name.acl_writeChunkToNBT);
        transformWriteChunkToNBT(cn, method);

        method = AsmUtil.findMethod(cn, Name.acl_readChunkFromNBT);
        transformReadChunkFromNBT(cn, method);
    }

    private void transformWriteChunkToNBT(ClassNode cn, MethodNode method) {
        InsnList code = method.instructions;

        for (ListIterator<AbstractInsnNode> iterator = code.iterator(); iterator.hasNext(); ) {
            AbstractInsnNode insn = iterator.next();

            if (insn.getOpcode() == Opcodes.LDC && ((LdcInsnNode) insn).cst.equals("Blocks")) {
                iterator.remove();
                iterator.next();
                iterator.next();
                iterator.remove(); // remove INVOKEVIRTUAL ExtendedBlockStorage.getBlockLSBArray
                iterator.next();
                iterator.remove(); // remove INVOKEVIRTUAL NBTTagCompound.setByteArray

                iterator.add(Name.hooks_writeChunkToNbt.invokeStatic());
                return;
            }
        }

        throw new AsmTransformException("can't find Blocks LDC");
    }

    private void transformReadChunkFromNBT(ClassNode cn, MethodNode method) {
        InsnList code = method.instructions;
        int part = 0;

        for (ListIterator<AbstractInsnNode> iterator = code.iterator(); iterator.hasNext(); ) {
            AbstractInsnNode insn = iterator.next();

            if (part == 0) {
                if (insn.getOpcode() == Opcodes.LDC && ((LdcInsnNode) insn).cst.equals("Blocks")) {
                    // ExtendedBlockStorage, NBTTagCompound are on the stack
                    iterator.set(Name.hooks_readChunkFromNbt.invokeStatic());
                    part++;
                }
            } else if (part == 1) {
                iterator.remove();

                if (insn.getOpcode() == Opcodes.INVOKEVIRTUAL) {
                    MethodInsnNode node = (MethodInsnNode) insn;

                    if (Name.ebs_setBlockMSBArray.matches(node)) part++;
                }
            } else {
                if (insn.getType() == AbstractInsnNode.FRAME) {
                    iterator.remove();
                    part++;
                    break;
                }
            }
        }

        if (part != 3) throw new AsmTransformException("no match for part " + part);
    }
}
