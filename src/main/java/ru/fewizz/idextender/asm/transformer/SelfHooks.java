package ru.fewizz.idextender.asm.transformer;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import ru.fewizz.idextender.asm.AsmUtil;
import ru.fewizz.idextender.asm.IClassNodeTransformer;
import ru.fewizz.idextender.asm.Name;

public class SelfHooks implements IClassNodeTransformer {
	@Override
	public void transform(ClassNode cn, boolean obfuscated) {
		MethodNode method = AsmUtil.findMethod(cn, "get");
		transformGet(cn, method);

		method = AsmUtil.findMethod(cn, "setBlockRefCount");
		transformSetBlockRefCount(cn, method, obfuscated);

		method = AsmUtil.findMethod(cn, "setTickRefCount");
		transformSetTickRefCount(cn, method, obfuscated);
	}

	private void transformGet(ClassNode cn, MethodNode method) {
		// replace with "return ebs.block16BArray;"
		InsnList code = method.instructions;

		code.clear();
		code.add(new VarInsnNode(Opcodes.ALOAD, 0));
		code.add(new FieldInsnNode(Opcodes.GETFIELD, Type.getArgumentTypes(method.desc)[0].getInternalName(), "block16BArray", "[S"));
		code.add(new InsnNode(Opcodes.ARETURN));

		method.localVariables = null;
		method.maxStack = 1;
	}

	private void transformSetBlockRefCount(ClassNode cn, MethodNode method, boolean isObf) {
		InsnList code = method.instructions;

		code.clear();
		code.add(new VarInsnNode(Opcodes.ALOAD, 0));
		code.add(new VarInsnNode(Opcodes.ILOAD, 1));
		code.add(Name.ebs_blockRefCount.virtualSet(isObf));
		code.add(new InsnNode(Opcodes.RETURN));

		method.localVariables = null;
		method.maxStack = 2;
	}

	private void transformSetTickRefCount(ClassNode cn, MethodNode method, boolean isObf) {
		InsnList code = method.instructions;

		code.clear();
		code.add(new VarInsnNode(Opcodes.ALOAD, 0));
		code.add(new VarInsnNode(Opcodes.ILOAD, 1));
		code.add(Name.ebs_tickRefCount.virtualSet(isObf));
		code.add(new InsnNode(Opcodes.RETURN));

		method.localVariables = null;
		method.maxStack = 2;
	}
}
