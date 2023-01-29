package ru.fewizz.idextender.asm.transformer;

import java.util.*;

import org.objectweb.asm.tree.*;

import ru.fewizz.idextender.asm.*;

public class VanillaExtendedBlockStorage implements IClassNodeTransformer {

    @Override
    public void transform(final ClassNode cn, final boolean obfuscated) {
        cn.fields.add(new FieldNode(1, "block16BArray", "[S", null, null));
        AsmUtil.makePublic(AsmUtil.findField(cn, Name.ebs_blockRefCount));
        AsmUtil.makePublic(AsmUtil.findField(cn, Name.ebs_tickRefCount));
        MethodNode method = AsmUtil.findMethod(cn, "<init>");
        this.transformConstructor(cn, method, obfuscated);
        method = AsmUtil.findMethod(cn, Name.ebs_getBlock);
        this.transformGetBlock(cn, method, obfuscated);
        method = AsmUtil.findMethod(cn, Name.ebs_setBlock);
        this.transformSetBlock(cn, method, obfuscated);
        method = AsmUtil.findMethod(cn, Name.ebs_getBlockMSBArray);
        this.transformGetBlockMSBArray(cn, method);
        method = AsmUtil.findMethod(cn, Name.ebs_removeInvalidBlocks);
        this.transformRemoveInvalidBlocks(cn, method);
    }

    private void transformConstructor(final ClassNode cn, final MethodNode method, final boolean obfuscated) {
        final InsnList code = method.instructions;
        final ListIterator<AbstractInsnNode> iterator = code.iterator();
        if (iterator.hasNext()) {
            AbstractInsnNode insn = iterator.next();
            insn = insn.getNext().getNext();
            final InsnList toInsert = new InsnList();
            toInsert.add(new VarInsnNode(25, 0));
            toInsert.add(Name.hooks_create16BArray.staticInvocation(obfuscated));
            toInsert.add(new FieldInsnNode(181, cn.name, "block16BArray", "[S"));
            method.instructions.insert(insn, toInsert);
        }
        method.maxStack = Math.max(method.maxStack, 2);
    }

    private void transformGetBlock(final ClassNode cn, final MethodNode method, final boolean obfuscated) {
        final InsnList code = method.instructions;
        code.clear();
        code.add(new VarInsnNode(25, 0));
        code.add(new VarInsnNode(21, 1));
        code.add(new VarInsnNode(21, 2));
        code.add(new VarInsnNode(21, 3));
        code.add(Name.hooks_getBlockById.staticInvocation(obfuscated));
        code.add(new InsnNode(176));
        method.localVariables = null;
        method.maxStack = 4;
    }

    private void transformSetBlock(final ClassNode cn, final MethodNode method, final boolean obfuscated) {
        final InsnList code = method.instructions;
        int part = 0;
        final ListIterator<AbstractInsnNode> iterator = code.iterator();
        while (iterator.hasNext()) {
            final AbstractInsnNode insn = iterator.next();
            if (part == 0) {
                iterator.remove();
                if (insn.getOpcode() != 184) {
                    continue;
                }
                ++part;
                iterator.add(new VarInsnNode(25, 0));
                iterator.add(new VarInsnNode(21, 1));
                iterator.add(new VarInsnNode(21, 2));
                iterator.add(new VarInsnNode(21, 3));
                iterator.add(Name.ebs_getBlock.virtualInvocation(obfuscated));
            } else if (part == 1) {
                if (insn.getOpcode() != 184) {
                    continue;
                }
                iterator.set(new VarInsnNode(25, 6));
                iterator.add(Name.hooks_getIdFromBlockWithCheck.staticInvocation(obfuscated));
                ++part;
            } else {
                iterator.remove();
            }
        }
        if (part != 2) {
            throw new AsmTransformException("no match for part " + part);
        }
        code.add(new VarInsnNode(54, 5));
        code.add(new VarInsnNode(25, 0));
        code.add(new VarInsnNode(21, 1));
        code.add(new VarInsnNode(21, 2));
        code.add(new VarInsnNode(21, 3));
        code.add(new VarInsnNode(21, 5));
        code.add(Name.hooks_setBlockId.staticInvocation(obfuscated));
        code.add(new InsnNode(177));
        method.localVariables = null;
        --method.maxLocals;
        method.maxStack = Math.max(method.maxStack, 5);
    }

    private void transformGetBlockMSBArray(final ClassNode cn, final MethodNode method) {
        final InsnList code = method.instructions;
        code.clear();
        code.add(new InsnNode(1));
        code.add(new InsnNode(176));
        method.localVariables = null;
        method.maxStack = 1;
    }

    private void transformRemoveInvalidBlocks(final ClassNode cn, final MethodNode method) {
        final InsnList code = method.instructions;
        code.clear();
        code.add(new VarInsnNode(25, 0));
        code.add(
                new MethodInsnNode(
                        184,
                        "ru/fewizz/idextender/Hooks",
                        "removeInvalidBlocksHook",
                        "(L" + cn.name + ";)V",
                        false));
        code.add(new InsnNode(177));
        method.localVariables = null;
        method.maxStack = 1;
    }
}
