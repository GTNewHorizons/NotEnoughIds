package ru.fewizz.idextender.asm.transformer;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import ru.fewizz.idextender.asm.AsmUtil;
import ru.fewizz.idextender.asm.IClassNodeTransformer;
import ru.fewizz.idextender.asm.Name;

public class VanillaNetHandlerPlayClient implements IClassNodeTransformer {
    @Override
    public void transform(ClassNode cn) {
        MethodNode method = AsmUtil.findMethod(cn, Name.nhpc_handleMultiBlockChange);
        method.maxStack += 2;

        InsnList code = method.instructions;

        AbstractInsnNode onGetBlock = AsmUtil.findMethodInsnNode(code.getFirst(), Name.block_getBlockByID);
        InsnList il = new InsnList();
        il.add(new VarInsnNode(ALOAD, 4));
        il.add(new VarInsnNode(ALOAD, 1));
        il.add(Name.s22_get_array.invokeVirtual());
        il.add(new InsnNode(ARRAYLENGTH));
        il.add(new VarInsnNode(ALOAD, 1));
        il.add(Name.s22_get_blocks_count.invokeVirtual());
        il.add(Name.hooks_handleMultiBlockChange_handleMultiBlockChange_readBlockIDAdditionalBitsIfNeed.invokeStatic());
        il.add(new IntInsnNode(BIPUSH, 12));
        il.add(new InsnNode(ISHL));
        il.add(new InsnNode(IOR));

        code.insertBefore(onGetBlock, il);
    }
}
