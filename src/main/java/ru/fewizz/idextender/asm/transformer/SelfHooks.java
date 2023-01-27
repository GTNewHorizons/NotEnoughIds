package ru.fewizz.idextender.asm.transformer;

import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;
import ru.fewizz.idextender.asm.*;

public class SelfHooks implements IClassNodeTransformer {
    @Override
    public void transform(final ClassNode cn, final boolean obfuscated) {
        MethodNode method = AsmUtil.findMethod(cn, "get");
        this.transformGet(cn, method);
        method = AsmUtil.findMethod(cn, "setBlockRefCount");
        this.transformSetBlockRefCount(cn, method, obfuscated);
        method = AsmUtil.findMethod(cn, "setTickRefCount");
        this.transformSetTickRefCount(cn, method, obfuscated);
    }

    private void transformGet(final ClassNode cn, final MethodNode method) {
        final InsnList code = method.instructions;
        code.clear();
        code.add(new VarInsnNode(Opcodes.ALOAD, 0));
        code.add(new FieldInsnNode(
                Opcodes.GETFIELD, Type.getArgumentTypes(method.desc)[0].getInternalName(), "block16BArray", "[S"));
        code.add(new InsnNode(Opcodes.ARETURN));
        method.localVariables = null;
        method.maxStack = 1;
    }

    private void transformSetBlockRefCount(final ClassNode cn, final MethodNode method, final boolean isObf) {
        final InsnList code = method.instructions;
        code.clear();
        code.add(new VarInsnNode(Opcodes.ALOAD, 0));
        code.add(new VarInsnNode(Opcodes.ILOAD, 1));
        code.add(Name.ebs_blockRefCount.virtualSet(isObf));
        code.add(new InsnNode(Opcodes.RETURN));
        method.localVariables = null;
        method.maxStack = 2;
    }

    private void transformSetTickRefCount(final ClassNode cn, final MethodNode method, final boolean isObf) {
        final InsnList code = method.instructions;
        code.clear();
        code.add(new VarInsnNode(Opcodes.ALOAD, 0));
        code.add(new VarInsnNode(Opcodes.ILOAD, 1));
        code.add(Name.ebs_tickRefCount.virtualSet(isObf));
        code.add(new InsnNode(Opcodes.RETURN));
        method.localVariables = null;
        method.maxStack = 2;
    }
}
