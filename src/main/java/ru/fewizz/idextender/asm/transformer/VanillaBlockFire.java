package ru.fewizz.idextender.asm.transformer;

import java.util.ListIterator;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.MethodNode;
import ru.fewizz.idextender.asm.IClassNodeTransformer;

public class VanillaBlockFire implements IClassNodeTransformer {
	@Override
	public boolean transform(ClassNode cn, boolean obfuscated) {
		for (MethodNode method : cn.methods) {
			InsnList code = method.instructions;

			for (ListIterator<AbstractInsnNode> iterator = code.iterator(); iterator.hasNext();) {
				AbstractInsnNode insn = iterator.next();

				if (insn.getOpcode() == Opcodes.SIPUSH && insn.getType() == AbstractInsnNode.INT_INSN
						&& ((IntInsnNode) insn).operand == 4096) {
					InsnList toInsert = new InsnList();
					toInsert.set(insn, new FieldInsnNode(Opcodes.GETSTATIC, "ru/fewizz/idextender/Hooks", "NUM_BLOCK_IDS", "I"));
					method.instructions.insert(toInsert);
				}
			}
		}

		return true;
	}
}
