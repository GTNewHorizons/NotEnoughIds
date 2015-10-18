package ru.fewizz.idextender.asm.transformer;

import java.util.ListIterator;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import ru.fewizz.idextender.asm.IClassNodeTransformer;

public class VanillaNetHandlerPlayClient implements IClassNodeTransformer {
	@Override
	public boolean transform(ClassNode cn, boolean obfuscated) {
		for (MethodNode method : cn.methods) {
			if ("handleMultiBlockChange".equals(method.name)
					|| ("a".equals(method.name) && "(Lgk;)V".equals(method.desc))) {
				InsnList code = method.instructions;
				for (ListIterator<AbstractInsnNode> iterator = code.iterator(); iterator.hasNext();) {
					AbstractInsnNode insn = iterator.next();

					if (insn.getOpcode() == Opcodes.SIPUSH && ((IntInsnNode) insn).operand == 4095) {
						InsnList toInsert = new InsnList();

						method.instructions.remove(insn.getPrevious().getPrevious().getPrevious().getPrevious()
								.getPrevious().getPrevious().getPrevious().getPrevious());
						method.instructions.remove(insn.getPrevious().getPrevious().getPrevious().getPrevious()
								.getPrevious().getPrevious().getPrevious());
						method.instructions.remove(insn.getPrevious().getPrevious().getPrevious().getPrevious()
								.getPrevious().getPrevious());

						insn = insn.getPrevious().getPrevious();
						method.instructions.remove(insn.getPrevious());
						insn = insn.getNext();
						method.instructions.remove(insn.getPrevious());
						insn = insn.getNext();
						method.instructions.remove(insn.getPrevious());
						insn = insn.getNext();
						method.instructions.remove(insn.getPrevious());

						toInsert.add(new VarInsnNode(Opcodes.ALOAD, 4));
						toInsert.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/io/DataInputStream",
								"readShort", "()S", false));
						toInsert.add(new LdcInsnNode(new Integer(65535)));
						method.instructions.insert(insn.getPrevious(), toInsert);

						toInsert.set(insn.getNext(), new VarInsnNode(Opcodes.ISTORE, 8));
						method.instructions.insert(toInsert);

						insn = insn.getNext().getNext().getNext().getNext();

						insn = insn.getNext();
						method.instructions.remove(insn.getPrevious());
						insn = insn.getNext();
						method.instructions.remove(insn.getPrevious());

						toInsert.add(new VarInsnNode(Opcodes.ALOAD, 4));
						toInsert.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/io/DataInputStream",
								"readByte", "()B", false));
						toInsert.add(new IntInsnNode(Opcodes.SIPUSH, 255));
						method.instructions.insert(insn.getPrevious(), toInsert);
					}
				}
			}
		}

		return true;
	}
}
