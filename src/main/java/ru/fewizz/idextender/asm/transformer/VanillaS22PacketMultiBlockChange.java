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
import ru.fewizz.idextender.asm.AsmTransformException;
import ru.fewizz.idextender.asm.AsmUtil;
import ru.fewizz.idextender.asm.IClassNodeTransformer;
import ru.fewizz.idextender.asm.Name;

public class VanillaS22PacketMultiBlockChange implements IClassNodeTransformer {
	@Override
	public void transform(ClassNode cn, boolean obfuscated) {
		MethodNode method = AsmUtil.findMethod(cn, Name.s22_init_server);

		InsnList code = method.instructions;
		int part = 0;

		for (ListIterator<AbstractInsnNode> iterator = code.iterator(); iterator.hasNext();) {
			AbstractInsnNode insn = iterator.next();

			if (part == 0) { // replace 4 * by 5 *
				if (insn.getOpcode() == Opcodes.ICONST_4) {
					iterator.set(new InsnNode(Opcodes.ICONST_5));
					part++;
				}
			} else if (part == 1) { // search Block.getIdFromBlock call, write result to the stream directly
				if (insn.getOpcode() == Opcodes.INVOKESTATIC) {
					MethodInsnNode node = (MethodInsnNode) insn;

					if (Name.block_getIdFromBlock.matches(node, obfuscated)) {
						iterator.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/io/DataOutputStream", "writeShort", "(I)V", false));
						part++;
					}
				}
			} else if (part == 2) { // remove everything up to ALOAD (exclusive)
				if (insn.getOpcode() == Opcodes.ALOAD) {
					part++;
				} else {
					iterator.remove();
				}
			} else if (part == 3) { // seek to the next INVOKEVIRTUAL (Chunk.getBlockMetadata), add stream byte write afterwards
				if (insn.getOpcode() == Opcodes.INVOKEVIRTUAL) {
					iterator.add(new VarInsnNode(Opcodes.ALOAD, 6)); // DataOutputStream
					iterator.add(new InsnNode(Opcodes.SWAP));
					iterator.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/io/DataOutputStream", "writeByte", "(I)V", false));
					part++;
				}
			} else { // remove everything up to INVOKEVIRTUAL (inclusive, DataOutputStream.writeShort)
				iterator.remove();

				if (insn.getOpcode() == Opcodes.INVOKEVIRTUAL) {
					return;
				}
			}
		}

		throw new AsmTransformException("no match for part "+part);
	}
}
