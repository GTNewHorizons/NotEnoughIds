package ru.fewizz.idextender.asm.transformer;

import java.util.ListIterator;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;
import ru.fewizz.idextender.asm.IClassNodeTransformer;

public class CofhBlockHelper implements IClassNodeTransformer {
	@Override
	public boolean transform(ClassNode cn, boolean obfuscated) {
		for (MethodNode method : cn.methods) {
			if ("<clinit>".equals(method.name)) {
				InsnList code = method.instructions;

				for (ListIterator<AbstractInsnNode> iterator = code.iterator(); iterator.hasNext();) {
					AbstractInsnNode insn = iterator.next();

					if (insn.getType() == AbstractInsnNode.INT_INSN && insn.getOpcode() == Opcodes.SIPUSH) {
						InsnList toInsert = new InsnList();

						toInsert.set(insn, new LdcInsnNode(new Integer(32768)));
						method.instructions.insert(toInsert);
						break;
					}
				}
			}
		}

		return true;
	}
}
