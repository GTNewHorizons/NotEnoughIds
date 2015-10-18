package ru.fewizz.idextender.asm.transformer;

import java.util.ListIterator;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import ru.fewizz.idextender.asm.IClassNodeTransformer;

public class VanillaS22PacketMultiBlockChange implements IClassNodeTransformer {
	@Override
	public boolean transform(ClassNode cn, boolean obfuscated) {
		for (MethodNode method : cn.methods) {
			if ("<init>".equals(method.name) || "S22PacketMultiBlockChange".equals(method.name)
					|| "(I[SLapx;)V".equals(method.desc)) {
				InsnList code = method.instructions;
				for (ListIterator<AbstractInsnNode> iterator = code.iterator(); iterator.hasNext();) {
					AbstractInsnNode insn = iterator.next();

					if (insn.getOpcode() == Opcodes.ICONST_4) {
						InsnList toInsert = new InsnList();
						toInsert.set(insn, new InsnNode(Opcodes.ICONST_5));
						method.instructions.insert(insn, toInsert);

					}
					if (insn.getOpcode() == Opcodes.INVOKEVIRTUAL
							&& ((MethodInsnNode) insn).name.equals(!obfuscated ? "getBlock" : "a")
							&& ((MethodInsnNode) insn).desc
							.equals(!obfuscated ? "(III)Lnet/minecraft/block/Block;" : "(III)Laji;")) {
						InsnList toInsert = new InsnList();

						insn = insn.getPrevious().getPrevious().getPrevious().getPrevious();

						for (int i = 0; i < 20; i++) {
							method.instructions.remove(insn.getPrevious());
							insn = insn.getNext();
						}
						method.instructions.remove(insn.getPrevious());

						toInsert.add(new VarInsnNode(Opcodes.ALOAD, 6));
						toInsert.add(new VarInsnNode(Opcodes.ALOAD, 3));
						toInsert.add(new VarInsnNode(Opcodes.ILOAD, 8));
						toInsert.add(new VarInsnNode(Opcodes.ILOAD, 10));
						toInsert.add(new VarInsnNode(Opcodes.ILOAD, 9));
						toInsert.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL,
								!obfuscated ? "net/minecraft/world/chunk/Chunk" : "apx", !obfuscated ? "getBlock" : "a",
										!obfuscated ? "(III)Lnet/minecraft/block/Block;" : "(III)Laji;", false));
						toInsert.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
								!obfuscated ? "net/minecraft/block/Block" : "aji", !obfuscated ? "getIdFromBlock" : "b",
										!obfuscated ? "(Lnet/minecraft/block/Block;)I" : "(Laji;)I", false));
						toInsert.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/io/DataOutputStream",
								"writeShort", "(I)V", false));

						toInsert.add(new VarInsnNode(Opcodes.ALOAD, 6));
						toInsert.add(new VarInsnNode(Opcodes.ALOAD, 3));
						toInsert.add(new VarInsnNode(Opcodes.ILOAD, 8));
						toInsert.add(new VarInsnNode(Opcodes.ILOAD, 10));
						toInsert.add(new VarInsnNode(Opcodes.ILOAD, 9));
						toInsert.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL,
								!obfuscated ? "net/minecraft/world/chunk/Chunk" : "apx",
										!obfuscated ? "getBlockMetadata" : "c", "(III)I", false));
						toInsert.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/io/DataOutputStream",
								"writeByte", "(I)V", false));

						method.instructions.insert(insn.getPrevious(), toInsert);
					}
				}
			}
		}

		return true;
	}
}
