package com.gtnewhorizons.neid.asm.transformer;

import java.util.ListIterator;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;

import com.gtnewhorizons.neid.Constants;
import com.gtnewhorizons.neid.asm.AsmUtil;
import com.gtnewhorizons.neid.asm.IClassNodeTransformer;

public class WorldEditBaseBlock implements IClassNodeTransformer {

    @Override
    public String[] getTargetClass() {
        return new String[] { "com.sk89q.worldedit.blocks.BaseBlock" };
    }

    public void transform(ClassNode cn, boolean obfuscated) {
        MethodNode method = AsmUtil.findMethod(cn, "internalSetId", true);
        if (method == null) return;
        AsmUtil.transformInlinedSizeMethod(cn, method, Constants.VANILLA_MAX_BLOCK_ID, Constants.MAX_BLOCK_ID);
        InsnList code = method.instructions;
        for (ListIterator<AbstractInsnNode> iterator = code.iterator(); iterator.hasNext();) {
            AbstractInsnNode insn = iterator.next();
            if (insn.getType() == 9 && ((LdcInsnNode) insn).cst instanceof String) {
                String string = (String) ((LdcInsnNode) insn).cst;
                if (string.contains(Integer.toString(Constants.VANILLA_MAX_BLOCK_ID))) {
                    ((LdcInsnNode) insn).cst = string.replace(
                            Integer.toString(Constants.VANILLA_MAX_BLOCK_ID),
                            Integer.toString(Constants.MAX_BLOCK_ID));
                    break;
                }
            }
        }
    }
}
