package ru.fewizz.idextender.asm.transformer;

import java.util.ListIterator;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.MethodNode;
import ru.fewizz.idextender.asm.IClassNodeTransformer;

public class VanillaS26PacketMapChunkBulk implements IClassNodeTransformer {
	@Override
	public boolean transform(ClassNode cn, boolean obfuscated) {
		for (MethodNode method : cn.methods) {
			if ("readPacketData".equals(method.name)
					|| ("a".equals(method.name) && "(Let;)V".equals(method.desc))) {
				InsnList code = method.instructions;

				for (ListIterator<AbstractInsnNode> iterator = code.iterator(); iterator.hasNext();) {
					AbstractInsnNode insn = iterator.next();

					if (insn.getOpcode() == Opcodes.SIPUSH && ((IntInsnNode) insn).operand == 8192) {

						InsnList toInsert = new InsnList();

						toInsert.set(insn, new IntInsnNode(Opcodes.SIPUSH, 12288));
						method.instructions.insert(toInsert);
						break;
					}
				}
			}
		}

		return true;
	}
}
