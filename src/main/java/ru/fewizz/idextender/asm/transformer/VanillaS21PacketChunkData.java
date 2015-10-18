package ru.fewizz.idextender.asm.transformer;

import java.util.ListIterator;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import ru.fewizz.idextender.asm.IClassNodeTransformer;

public class VanillaS21PacketChunkData implements IClassNodeTransformer {
	@Override
	public boolean transform(ClassNode cn, boolean obfuscated) {
		for (MethodNode method : cn.methods) {
			InsnList code2 = method.instructions;
			for (ListIterator<AbstractInsnNode> iterator = code2.iterator(); iterator.hasNext();) {
				AbstractInsnNode insn = iterator.next();

				if (insn.getType() == AbstractInsnNode.LDC_INSN && ((LdcInsnNode) insn).cst.equals(new Integer(196864))) {
					InsnList toInsert = new InsnList();

					toInsert.set(insn, new LdcInsnNode(new Integer(new Integer(262144))));
					method.instructions.insert(insn, toInsert);
				}
			}

			if ("func_149269_a".equals(method.name)
					|| ("a".equals(method.name) && "(Lapx;ZI)Lgy;".equals(method.desc))) {
				InsnList code = method.instructions;

				for (ListIterator<AbstractInsnNode> iterator = code.iterator(); iterator.hasNext();) {
					AbstractInsnNode insn = iterator.next();

					if (insn.getOpcode() == Opcodes.INVOKEVIRTUAL
							&& ((MethodInsnNode) insn).name.equals(!obfuscated ? "getBlockLSBArray" : "g")) {
						InsnList toInsert = new InsnList();

						toInsert.set(insn,
								new MethodInsnNode(Opcodes.INVOKESTATIC, "ru/fewizz/idextender/Hooks",
										"get16BitBlockArray",
										!obfuscated ? "(Lnet/minecraft/world/chunk/storage/ExtendedBlockStorage;)[B"
												: "(Lapz;)[B",
												false));
						toInsert.insert(toInsert);
					}

					if (insn.getOpcode() == Opcodes.ILOAD && insn.getNext().getOpcode() == Opcodes.IFLE) {

						InsnList toInsert = new InsnList();
						toInsert.set(insn, new MethodInsnNode(Opcodes.INVOKESTATIC, "ru/fewizz/idextender/Hooks",
								"zero", "()I", false));
						method.instructions.insert(toInsert);

					}
				}
			}
		}

		return true;
	}
}
