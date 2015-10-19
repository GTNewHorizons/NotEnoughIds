package ru.fewizz.idextender.asm.transformer;

import java.util.ListIterator;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import ru.fewizz.idextender.asm.AsmUtil;
import ru.fewizz.idextender.asm.Constants;
import ru.fewizz.idextender.asm.IClassNodeTransformer;
import ru.fewizz.idextender.asm.Name;

public class VanillaChunk implements IClassNodeTransformer {
	@Override
	public boolean transform(ClassNode cn, boolean obfuscated) {
		MethodNode method = AsmUtil.findMethod(cn, Name.chunk_fillChunk);
		if (method == null) return false;

		method.localVariables = null;

		int part = 0;
		LabelNode endLabel = null;

		for (ListIterator<AbstractInsnNode> it = method.instructions.iterator(); it.hasNext();) {
			AbstractInsnNode insn = it.next();

			if (part == 0) { // find getBlockLSBArray() call, replace with setting the data
				if (insn.getOpcode() == Opcodes.INVOKEVIRTUAL) {
					MethodInsnNode node = (MethodInsnNode) insn;

					if (node.owner.equals(Name.extendedBlockStorage.get(obfuscated)) &&
							node.name.equals(Name.ebs_getBlockLSBArray.get(obfuscated)) &&
							node.desc.equals(Name.ebs_getBlockLSBArray.getDesc(obfuscated))) {
						// ExtendedBlockStorage is on the stack
						it.set(new VarInsnNode(Opcodes.ALOAD, 1)); // replace with data (byte[]) load
						it.add(new VarInsnNode(Opcodes.ILOAD, 6)); // offset (k)
						it.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
								Name.hooks.get(obfuscated),
								Name.hooks_setBlockData.get(obfuscated),
								Name.hooks_setBlockData.getDesc(obfuscated), false));
						part++;
					}
				}
			} else if (part == 1) { // remove everything up to IADD (exclusive), insert add operands (k, Constants.ebsIdArraySize)
				if (insn.getOpcode() == Opcodes.IADD) {
					it.set(new VarInsnNode(Opcodes.ILOAD, 6));
					it.add(new LdcInsnNode(Constants.ebsIdArraySize));
					it.add(new InsnNode(Opcodes.IADD));
					part++;
				} else {
					it.remove();
				}
			} else if (part == 2) { // seek to ExtendedBlockStorage.getBlockMSBArray, then back to ICONST_0 or the preceding label
				if (insn.getOpcode() == Opcodes.INVOKEVIRTUAL) {
					MethodInsnNode node = (MethodInsnNode) insn;

					if (node.owner.equals(Name.extendedBlockStorage.get(obfuscated)) &&
							node.name.equals(Name.ebs_getBlockMSBArray.get(obfuscated)) &&
							node.desc.equals(Name.ebs_getBlockMSBArray.getDesc(obfuscated))) {
						while (it.previous().getOpcode() != Opcodes.ICONST_0);
						while (it.previous().getType() != AbstractInsnNode.LABEL);

						it.next(); // reverse the iterator, returns the label again
						part++;
					}
				}
			} else { // remove everything up to the end label (exclusive), detect the end label from the 1st IF_ICMPGE insn (main loop condition)
				if (endLabel == null) {
					if (insn.getOpcode() == Opcodes.IF_ICMPGE) {
						endLabel = ((JumpInsnNode) insn).label;
					}

					it.remove();
				} else {
					if (insn == endLabel) {
						return true;
					} else {
						it.remove();
					}
				}
			}
		}

		return false;
	}
}
