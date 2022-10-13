package ru.fewizz.idextender.asm.transformer;

import java.util.ListIterator;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import ru.fewizz.idextender.asm.AsmUtil;
import ru.fewizz.idextender.asm.IClassNodeTransformer;
import ru.fewizz.idextender.asm.Name;

public class UndergroundBiomesBiomeUndergroundDecorator implements IClassNodeTransformer {
    @Override
    public void transform(ClassNode cn) {
        MethodNode method = AsmUtil.findMethod(cn, Name.ub_bud_replaceChunkOres_world);
        transformReplaceChunkOres(cn, method, 1);

        method = AsmUtil.findMethod(cn, Name.ub_bud_replaceChunkOres_iChunkProvider);
        transformReplaceChunkOres(cn, method, 0);
    }

    private void transformReplaceChunkOres(ClassNode cn, MethodNode method, int varOffset) {
        InsnList code = method.instructions;

        for (ListIterator<AbstractInsnNode> iterator = code.iterator(); iterator.hasNext(); ) {
            AbstractInsnNode insn = iterator.next();

            if (insn.getOpcode() == Opcodes.INVOKEVIRTUAL
                    && Name.ebs_getBlockLSBArray.matches((MethodInsnNode)
                            insn)) { // replacing: int blockID = extendedblockstorage.getBlockLSBArray()[(inLevelY << 8
                // | chunkz << 4 | chunkx)] & 0xFF;
                iterator.remove();
                // ExtendedBlockStorage is on the stack
                iterator.add(new VarInsnNode(Opcodes.ILOAD, 9 + varOffset)); // x
                iterator.add(new VarInsnNode(Opcodes.ILOAD, 18 + varOffset)); // y
                iterator.add(new VarInsnNode(Opcodes.ILOAD, 10 + varOffset)); // z
                iterator.add(Name.hooks_getBlockId.invokeStatic());
                while (iterator.next().getOpcode() != Opcodes.ISTORE) iterator.remove();
                break;
            }
        }
    }
}
