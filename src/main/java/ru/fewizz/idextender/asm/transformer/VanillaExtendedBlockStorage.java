package ru.fewizz.idextender.asm.transformer;

import java.util.ListIterator;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import ru.fewizz.idextender.asm.AsmTransformException;
import ru.fewizz.idextender.asm.AsmUtil;
import ru.fewizz.idextender.asm.IClassNodeTransformer;
import ru.fewizz.idextender.asm.Name;

public class VanillaExtendedBlockStorage implements IClassNodeTransformer {
	@Override
	public void transform(ClassNode cn, boolean obfuscated) {
		cn.fields.add(new FieldNode(Opcodes.ACC_PUBLIC, "block16BArray", "[S", null, null));

		AsmUtil.makePublic(AsmUtil.findField(cn, Name.ebs_blockRefCount));
		AsmUtil.makePublic(AsmUtil.findField(cn, Name.ebs_tickRefCount));

		MethodNode method = AsmUtil.findMethod(cn, "<init>");
		transformConstructor(cn, method, obfuscated);

		method = AsmUtil.findMethod(cn, Name.ebs_getBlock);
		transformGetBlock(cn, method, obfuscated);

		method = AsmUtil.findMethod(cn, Name.ebs_setBlock);
		transformSetBlock(cn, method, obfuscated);

		method = AsmUtil.findMethod(cn, Name.ebs_getBlockMSBArray);
		transformGetBlockMSBArray(cn, method);

		method = AsmUtil.findMethod(cn, Name.ebs_removeInvalidBlocks);
		transformRemoveInvalidBlocks(cn, method);
	}

	private void transformConstructor(ClassNode cn, MethodNode method, boolean obfuscated) {
		// add block16BArray initialization by forwarding to Hooks.create16BArray
		InsnList code = method.instructions;

		for (ListIterator<AbstractInsnNode> iterator = code.iterator(); iterator.hasNext();) {
			AbstractInsnNode insn = iterator.next();

			insn = insn.getNext().getNext();

			InsnList toInsert = new InsnList();

			toInsert.add(new VarInsnNode(Opcodes.ALOAD, 0));
			toInsert.add(Name.hooks_create16BArray.staticInvocation(obfuscated));
			toInsert.add(new FieldInsnNode(Opcodes.PUTFIELD, cn.name, "block16BArray", "[S"));

			method.instructions.insert(insn, toInsert);

			break;
		}

		method.maxStack = Math.max(method.maxStack, 2);
	}

	private void transformGetBlock(ClassNode cn, MethodNode method, boolean obfuscated) {
		// redirect to Hooks.getBlockById
		InsnList code = method.instructions;

		code.clear();
		code.add(new VarInsnNode(Opcodes.ALOAD, 0));
		code.add(new VarInsnNode(Opcodes.ILOAD, 1));
		code.add(new VarInsnNode(Opcodes.ILOAD, 2));
		code.add(new VarInsnNode(Opcodes.ILOAD, 3));
		code.add(Name.hooks_getBlockById.staticInvocation(obfuscated));
		code.add(new InsnNode(Opcodes.ARETURN));

		method.localVariables = null;
		method.maxStack = 4;
	}

	private void transformSetBlock(ClassNode cn, MethodNode method, boolean obfuscated) {
		// this will remove everything but the ref count manipulation and delegate to Hooks.setBlock
		InsnList code = method.instructions;
		int part = 0;

		for (ListIterator<AbstractInsnNode> iterator = code.iterator(); iterator.hasNext();) {
			AbstractInsnNode insn = iterator.next();

			if (part == 0) { // remove everything up to the Block.getBlockById call (inclusive)
				iterator.remove();

				if (insn.getOpcode() == Opcodes.INVOKESTATIC) {
					part++;

					iterator.add(new VarInsnNode(Opcodes.ALOAD, 0));
					iterator.add(new VarInsnNode(Opcodes.ILOAD, 1));
					iterator.add(new VarInsnNode(Opcodes.ILOAD, 2));
					iterator.add(new VarInsnNode(Opcodes.ILOAD, 3));
					iterator.add(Name.ebs_getBlock.virtualInvocation(obfuscated));
				}
			}
			else if (part == 1) { // seek to the Block.getIdFromBlock call
				if (insn.getOpcode() == Opcodes.INVOKESTATIC) {
					iterator.set(Name.hooks_getIdFromBlockWithCheck.staticInvocation(obfuscated));
					part++;
				}
			}
			else { // remove the remainder
				iterator.remove();
			}
		}

		if (part != 2)
			throw new AsmTransformException("no match for part " + part);

		// the block id is still on the stack from the previous INVOKESTATIC (Block.getIdFromBlock)
		code.add(new VarInsnNode(Opcodes.ISTORE, 5));

		code.add(new VarInsnNode(Opcodes.ALOAD, 0)); // ebs
		code.add(new VarInsnNode(Opcodes.ILOAD, 1)); // x
		code.add(new VarInsnNode(Opcodes.ILOAD, 2)); // y
		code.add(new VarInsnNode(Opcodes.ILOAD, 3)); // z
		code.add(new VarInsnNode(Opcodes.ILOAD, 5)); // block id
		code.add(Name.hooks_setBlockId.staticInvocation(obfuscated));
		code.add(new InsnNode(Opcodes.RETURN));

		method.localVariables = null;
		method.maxLocals--; // var 7 is now unused
		method.maxStack = Math.max(method.maxStack, 5);
	}

	private void transformGetBlockMSBArray(ClassNode cn, MethodNode method) {
		// always return null
		InsnList code = method.instructions;

		code.clear();
		code.add(new InsnNode(Opcodes.ACONST_NULL));
		code.add(new InsnNode(Opcodes.ARETURN));

		method.localVariables = null;
		method.maxStack = 1;
	}

	private void transformRemoveInvalidBlocks(ClassNode cn, MethodNode method) {
		InsnList code = method.instructions;

		code.clear();
		code.add(new VarInsnNode(Opcodes.ALOAD, 0));
		code.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "ru/fewizz/idextender/Hooks", "removeInvalidBlocksHook", "(L" + cn.name + ";)V", false));
		code.add(new InsnNode(Opcodes.RETURN));

		method.localVariables = null;
		method.maxStack = 1;
	}
}
