package ru.fewizz.idextender.asm.transformer;

import java.util.ListIterator;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import ru.fewizz.idextender.asm.IClassNodeTransformer;

public class VanillaChunk implements IClassNodeTransformer {
	@Override
	public boolean transform(ClassNode cn, boolean obfuscated) {
		for (MethodNode method : cn.methods) {
			if ("fillChunk".equals(method.name) || ("a".equals(method.name) && "([BIIZ)V".equals(method.desc))) {
				InsnList code = method.instructions;

				int i = 0;

				for (ListIterator<AbstractInsnNode> iterator = code.iterator(); iterator.hasNext();) {
					AbstractInsnNode insn = iterator.next();

					if (insn.getOpcode() == Opcodes.ALOAD && insn.getNext().getOpcode() == Opcodes.GETFIELD
							&& insn.getNext().getNext().getOpcode() == Opcodes.ILOAD
							&& insn.getNext().getNext().getNext().getOpcode() == Opcodes.AALOAD) {
						if (i != 1) {
							i++;
						} else {
							InsnList toInsert = new InsnList();

							insn = insn.getNext();
							method.instructions.remove(insn.getPrevious());
							insn = insn.getNext();
							method.instructions.remove(insn.getPrevious());
							insn = insn.getNext();
							method.instructions.remove(insn.getPrevious());
							insn = insn.getNext();
							method.instructions.remove(insn.getPrevious());
							insn = insn.getNext();
							method.instructions.remove(insn.getPrevious());

							toInsert.add(new IntInsnNode(Opcodes.SIPUSH, 8192));
							toInsert.add(new IntInsnNode(Opcodes.NEWARRAY, Opcodes.T_BYTE));
							method.instructions.insert(insn.getPrevious(), toInsert);

							toInsert.add(new VarInsnNode(Opcodes.ALOAD, 9));
							toInsert.add(new VarInsnNode(Opcodes.ALOAD, 0));
							toInsert.add(new FieldInsnNode(Opcodes.GETFIELD,
									!obfuscated ? "net/minecraft/world/chunk/Chunk" : "apx",
											!obfuscated ? "storageArrays" : "u",
													!obfuscated ? "[Lnet/minecraft/world/chunk/storage/ExtendedBlockStorage;"
															: "[Lapz;"));
							toInsert.add(new VarInsnNode(Opcodes.ILOAD, 8));
							toInsert.add(new InsnNode(Opcodes.AALOAD));
							toInsert.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "ru/fewizz/idextender/Hooks",
									"set16BitBlockArray",
									!obfuscated ? "([BLnet/minecraft/world/chunk/storage/ExtendedBlockStorage;)V"
											: "([BLapz;)V",
											false));
							method.instructions.insert(insn.getNext().getNext().getNext().getNext().getNext()
									.getNext().getNext().getNext().getNext().getNext().getNext().getNext().getNext()
									.getNext().getNext().getNext(), toInsert);
							break;
						}
					}
				}
			}
		}

		for (MethodNode method : cn.methods) {
			if ("fillChunk".equals(method.name) || ("a".equals(method.name) && "([BIIZ)V".equals(method.desc))) {
				InsnList code = method.instructions;

				int i1 = 0;

				for (ListIterator<AbstractInsnNode> iterator = code.iterator(); iterator.hasNext();) {
					AbstractInsnNode insn = iterator.next();

					if (insn.getOpcode() == Opcodes.ICONST_0) {
						if (i1 != 10) {
							i1++;
						} else {
							InsnList toInsert = new InsnList();

							toInsert.set(insn, new LdcInsnNode(new Integer(1000000)));
							method.instructions.insert(toInsert);
							break;
						}
					}
				}
			}
		}

		return true;
	}
}
