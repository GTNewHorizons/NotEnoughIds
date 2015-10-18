package ru.fewizz.idextender.asm.transformer;

import java.util.ListIterator;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import ru.fewizz.idextender.asm.IClassNodeTransformer;

public class VanillaAnvilChunkLoader implements IClassNodeTransformer {
	@Override
	public boolean transform(ClassNode cn, boolean obfuscated) {
		for (MethodNode method : cn.methods) {
			if ("writeChunkToNBT".equals(method.name)
					|| ("a".equals(method.name) && "(Lapx;Lahb;Ldh;)V".equals(method.desc))) {
				InsnList code = method.instructions;

				for (ListIterator<AbstractInsnNode> iterator = code.iterator(); iterator.hasNext();) {
					AbstractInsnNode insn = iterator.next();

					if (insn.getType() == AbstractInsnNode.LDC_INSN && ((LdcInsnNode) insn).cst.equals("Y")) {
						insn = insn.getNext().getNext().getNext().getNext().getNext().getNext().getNext().getNext()
								.getNext().getNext().getNext();
						while (true) {
							if (insn.getNext().getOpcode() == Opcodes.INVOKEVIRTUAL
									&& ((MethodInsnNode) insn.getNext()).name.equals(!obfuscated ? "setByteArray" : "a")
									&& ((MethodInsnNode) insn.getNext()).desc.equals("(Ljava/lang/String;[B)V")) {
								// && (insn.getOpcode() == Opcodes.GETFIELD && insn.getOpcode() == Opcodes.INVOKEVIRTUAL)){
								insn = insn.getNext();
								method.instructions.remove(insn.getPrevious());
								insn = insn.getNext();
								method.instructions.remove(insn.getPrevious());
								break;
							}
							insn = insn.getNext();
							method.instructions.remove(insn.getPrevious());
						}

						InsnList toInsert = new InsnList();
						toInsert.add(new VarInsnNode(Opcodes.ALOAD, 11));
						toInsert.add(new VarInsnNode(Opcodes.ALOAD, 9));
						toInsert.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "ru/fewizz/idextender/Hooks",
								"writeChunkToNbt",
								!obfuscated ? "(Lnet/minecraft/world/chunk/storage/ExtendedBlockStorage;Lnet/minecraft/nbt/NBTTagCompound;)V"
										: "(Lapz;Ldh;)V",
										false));
						method.instructions.insert(insn.getNext(), toInsert);
						break;
					}
				}
			}

			if ("readChunkFromNBT".equals(method.name)
					|| ("a".equals(method.name) && "(Lahb;Ldh;)Lapx;".equals(method.desc))) {
				InsnList code = method.instructions;

				for (ListIterator<AbstractInsnNode> iterator = code.iterator(); iterator.hasNext();) {
					AbstractInsnNode insn = iterator.next();

					if (insn.getOpcode() == Opcodes.ILOAD && ((VarInsnNode) insn).var == 9
							&& insn.getNext().getOpcode() == Opcodes.INVOKESPECIAL) {

						insn = insn.getNext().getNext().getNext();
						while (true) {
							if (insn.getNext().getOpcode() == Opcodes.INVOKEVIRTUAL
									&& ((MethodInsnNode) insn.getNext()).name
									.equals(!obfuscated ? "setBlockMSBArray" : "a")
									&& ((MethodInsnNode) insn.getNext()).desc.equals(
											!obfuscated ? "(Lnet/minecraft/world/chunk/NibbleArray;)V" : "(Lapv;)V")) {// (Lnet/minecraft/world/chunk/NibbleArray;)V
								insn = insn.getNext();
								method.instructions.remove(insn.getPrevious());
								insn = insn.getNext();
								method.instructions.remove(insn.getPrevious());
								break;
							}
							insn = insn.getNext();
							method.instructions.remove(insn.getPrevious());
						}

						InsnList toInsert = new InsnList();
						toInsert.add(new VarInsnNode(Opcodes.ALOAD, 13));
						toInsert.add(new VarInsnNode(Opcodes.ALOAD, 11));
						toInsert.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "ru/fewizz/idextender/Hooks",
								"readChunkFromNbt",
								!obfuscated ? "(Lnet/minecraft/world/chunk/storage/ExtendedBlockStorage;Lnet/minecraft/nbt/NBTTagCompound;)V"
										: "(Lapz;Ldh;)V",
										false));
						method.instructions.insert(insn.getNext(), toInsert);
						break;
					}
				}
				break;
			}
		}

		return true;
	}
}
