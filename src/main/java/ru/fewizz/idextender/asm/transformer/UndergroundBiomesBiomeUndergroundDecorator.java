package ru.fewizz.idextender.asm.transformer;

import java.util.ListIterator;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import ru.fewizz.idextender.asm.IClassNodeTransformer;

public class UndergroundBiomesBiomeUndergroundDecorator implements IClassNodeTransformer {
	@Override
	public boolean transform(ClassNode cn, boolean obfuscated) {
		for (MethodNode method : cn.methods) {
			if ("replaceChunkOres".equals(method.name) && method.desc.equals("(IILnet/minecraft/world/World;)V")) {
				InsnList code = method.instructions;

				for (ListIterator<AbstractInsnNode> iterator = code.iterator(); iterator.hasNext();) {
					AbstractInsnNode insn = iterator.next();

					if (insn.getOpcode() == Opcodes.INVOKEVIRTUAL
							&& ((MethodInsnNode) insn).name.equals(!obfuscated ? "getBlockLSBArray" : "func_76658_g")) {
						InsnList toInsert = new InsnList();
						toInsert.set(insn.getPrevious(), new VarInsnNode(Opcodes.ALOAD, 18));
						method.instructions.insert(toInsert);
						insn = insn.getNext();
						for (int i = 0; insn.getPrevious().getOpcode() != Opcodes.ISTORE; i++) {
							System.out.println("Ster " + i);
							method.instructions.remove(insn.getPrevious());
							insn = insn.getNext();
						}

						method.instructions.remove(insn.getPrevious());

						toInsert.add(new VarInsnNode(Opcodes.ILOAD, 10));
						toInsert.add(new VarInsnNode(Opcodes.ILOAD, 19));
						toInsert.add(new VarInsnNode(Opcodes.ILOAD, 11));
						toInsert.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "ru/fewizz/idextender/Hooks", "setID",
								"(Lnet/minecraft/world/chunk/storage/ExtendedBlockStorage;III)I", false));
						toInsert.add(new VarInsnNode(Opcodes.ISTORE, 20));
						method.instructions.insert(insn.getPrevious(), toInsert);

						insn = insn.getNext();

						while (insn.getNext().getOpcode() != Opcodes.GETFIELD) {
							method.instructions.remove(insn.getPrevious());
							insn = insn.getNext();
						}

						break;
					}
				}
			}

			if ("replaceChunkOres".equals(method.name)
					&& method.desc.equals("(Lnet/minecraft/world/chunk/IChunkProvider;II)V")) {
				InsnList code = method.instructions;

				for (ListIterator<AbstractInsnNode> iterator = code.iterator(); iterator.hasNext();) {
					AbstractInsnNode insn = iterator.next();

					if (insn.getOpcode() == Opcodes.INVOKEVIRTUAL
							&& ((MethodInsnNode) insn).name.equals(!obfuscated ? "getBlockLSBArray" : "func_76658_g")) {
						InsnList toInsert = new InsnList();
						toInsert.set(insn.getPrevious(), new VarInsnNode(Opcodes.ALOAD, 17));
						method.instructions.insert(toInsert);
						insn = insn.getNext();
						for (int i = 0; insn.getPrevious().getOpcode() != Opcodes.ISTORE; i++) {
							System.out.println("Ster " + i);
							method.instructions.remove(insn.getPrevious());
							insn = insn.getNext();
						}

						method.instructions.remove(insn.getPrevious());

						toInsert.add(new VarInsnNode(Opcodes.ILOAD, 9));
						toInsert.add(new VarInsnNode(Opcodes.ILOAD, 18));
						toInsert.add(new VarInsnNode(Opcodes.ILOAD, 10));
						toInsert.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "ru/fewizz/idextender/Hooks", "setID",
								"(Lnet/minecraft/world/chunk/storage/ExtendedBlockStorage;III)I", false));
						toInsert.add(new VarInsnNode(Opcodes.ISTORE, 19));
						method.instructions.insert(insn.getPrevious(), toInsert);

						insn = insn.getNext();

						while (insn.getNext().getOpcode() != Opcodes.GETFIELD) {
							method.instructions.remove(insn.getPrevious());
							insn = insn.getNext();
						}
						break;
					}
				}
			}
		}

		return true;
	}
}
