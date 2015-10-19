package ru.fewizz.idextender.asm.transformer;

import java.util.ListIterator;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;
import ru.fewizz.idextender.asm.Constants;
import ru.fewizz.idextender.asm.IClassNodeTransformer;

public class VanillaBlockFire implements IClassNodeTransformer {
	@Override
	public boolean transform(ClassNode cn, boolean obfuscated) {
		for (MethodNode method : cn.methods) {
			InsnList code = method.instructions;

			for (ListIterator<AbstractInsnNode> iterator = code.iterator(); iterator.hasNext();) {
				AbstractInsnNode insn = iterator.next();

				if (insn.getType() == AbstractInsnNode.INT_INSN && ((IntInsnNode) insn).operand == 4096) {
					iterator.set(new LdcInsnNode(Constants.maxBlockId + 1));
				}
			}
		}

		return true;
	}
}
