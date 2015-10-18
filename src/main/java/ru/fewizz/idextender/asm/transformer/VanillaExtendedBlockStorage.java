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
import ru.fewizz.idextender.asm.IClassNodeTransformer;

public class VanillaExtendedBlockStorage implements IClassNodeTransformer {
	@Override
	public boolean transform(ClassNode cn, boolean obfuscated) {
		cn.fields.add(new FieldNode(Opcodes.ACC_PUBLIC, "block16BArray", "[S", null, null));

		for (MethodNode method : cn.methods) {
			if ("<init>".equals(method.name) || "ExtendedBlockStorage".equals(method.name)) {
				InsnList code = method.instructions;

				for (ListIterator<AbstractInsnNode> iterator = code.iterator(); iterator.hasNext();) {
					AbstractInsnNode insn = iterator.next();

					insn = insn.getNext().getNext();

					InsnList toInsert = new InsnList();

					toInsert.add(new VarInsnNode(Opcodes.ALOAD, 0));
					toInsert.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "ru/fewizz/idextender/Hooks",
							"create16BArray", "()[S", false));
					toInsert.add(new FieldInsnNode(Opcodes.PUTFIELD,
							!obfuscated ? "net/minecraft/world/chunk/storage/ExtendedBlockStorage" : "apz",
									"block16BArray", "[S"));
					method.instructions.insert(insn, toInsert);
					break;
				}

			}

			if ("getBlockByExtId".equals(method.name)
					|| ("a".equals(method.name) && "(III)Laji;".equals(method.desc))) {
				InsnList code = method.instructions;

				for (ListIterator<AbstractInsnNode> iterator = code.iterator(); iterator.hasNext();) {
					AbstractInsnNode insn = iterator.next();

					insn = insn.getNext().getNext();

					InsnList toInsert = new InsnList();
					toInsert.add(new VarInsnNode(Opcodes.ILOAD, 1));
					toInsert.add(new VarInsnNode(Opcodes.ILOAD, 2));
					toInsert.add(new VarInsnNode(Opcodes.ILOAD, 3));
					toInsert.add(new VarInsnNode(Opcodes.ALOAD, 0));
					toInsert.add(
							new MethodInsnNode(Opcodes.INVOKESTATIC, "ru/fewizz/idextender/Hooks", "getBlockById",
									!obfuscated ? "(IIILnet/minecraft/world/chunk/storage/ExtendedBlockStorage;)Lnet/minecraft/block/Block;"
											: "(IIILapz;)Laji;",
											false));
					toInsert.add(new InsnNode(Opcodes.ARETURN));
					method.instructions.insert(insn, toInsert);
					break;
				}
			}

			if ("func_150818_a".equals(method.name)
					|| ("a".equals(method.name) && "(IIILaji;)V".equals(method.desc))) {
				InsnList code = method.instructions;

				for (ListIterator<AbstractInsnNode> iterator = code.iterator(); iterator.hasNext();) {
					AbstractInsnNode insn = iterator.next();

					if (insn.getOpcode() == Opcodes.F_APPEND && insn.getNext().getOpcode() == Opcodes.ILOAD
							&& ((VarInsnNode) insn.getNext()).var == 5) {
						InsnList toInsert = new InsnList();

						toInsert.set(insn, new VarInsnNode(Opcodes.ALOAD, 0));
						insn = insn.getNext();
						toInsert.set(insn, new VarInsnNode(Opcodes.ILOAD, 1));
						method.instructions.insert(toInsert);

						toInsert.add(new VarInsnNode(Opcodes.ILOAD, 2));
						toInsert.add(new VarInsnNode(Opcodes.ILOAD, 3));
						toInsert.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL,
								!obfuscated ? "net/minecraft/world/chunk/storage/ExtendedBlockStorage" : "apz",
										!obfuscated ? "getBlockByExtId" : "a",
												!obfuscated ? "(III)Lnet/minecraft/block/Block;" : "(III)Laji;", false));
						method.instructions.insert(insn, toInsert);
					}

					if (insn.getOpcode() == Opcodes.INVOKESTATIC
							&& ((MethodInsnNode) insn).name.equals(!obfuscated ? "getIdFromBlock" : "b")
							&& insn.getNext().getOpcode() == Opcodes.ISTORE
							&& ((VarInsnNode) insn.getNext()).var == 7 && ((MethodInsnNode) insn).desc
							.equals(!obfuscated ? "(Lnet/minecraft/block/Block;)I" : "(Laji;)I")) {
						InsnList toInsert = new InsnList();

						toInsert.add(new VarInsnNode(Opcodes.ILOAD, 7));
						toInsert.add(new VarInsnNode(Opcodes.ILOAD, 1));
						toInsert.add(new VarInsnNode(Opcodes.ILOAD, 2));
						toInsert.add(new VarInsnNode(Opcodes.ILOAD, 3));
						toInsert.add(new VarInsnNode(Opcodes.ALOAD, 0));
						toInsert.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "ru/fewizz/idextender/Hooks", "hook1",
								!obfuscated ? "(IIIILnet/minecraft/world/chunk/storage/ExtendedBlockStorage;)V"
										: "(IIIILapz;)V",
										false));
						method.instructions.insert(insn.getNext(), toInsert);
						break;
					}
				}
			}

			if ("getBlockMSBArray".equals(method.name)
					|| ("i".equals(method.name) && "()Lapv;".equals(method.desc))) {
				InsnList code = method.instructions;

				for (ListIterator<AbstractInsnNode> iterator = code.iterator(); iterator.hasNext();) {
					AbstractInsnNode insn = iterator.next();
					InsnList toInsert = new InsnList();
					insn = insn.getNext().getNext().getNext();
					toInsert.add(new InsnNode(Opcodes.ACONST_NULL));
					toInsert.add(new InsnNode(Opcodes.ARETURN));
					method.instructions.insert(toInsert);
					break;
				}
			}

			if ("isEmpty".equals(method.name) || ("a".equals(method.name) && "()Z".equals(method.desc))) {
				InsnList code = method.instructions;

				for (ListIterator<AbstractInsnNode> iterator = code.iterator(); iterator.hasNext();) {
					AbstractInsnNode insn = iterator.next();
					InsnList toInsert = new InsnList();
					toInsert.add(new InsnNode(Opcodes.ICONST_0));
					toInsert.add(new InsnNode(Opcodes.IRETURN));
					method.instructions.insert(insn.getNext().getNext(), toInsert);
					break;
				}
			}
		}

		return true;
	}
}
