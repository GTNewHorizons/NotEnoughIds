package ru.fewizz.idextender.asm.transformer;

import java.util.ListIterator;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import ru.fewizz.idextender.asm.AsmTransformException;
import ru.fewizz.idextender.asm.AsmUtil;
import ru.fewizz.idextender.asm.Constants;
import ru.fewizz.idextender.asm.IClassNodeTransformer;
import ru.fewizz.idextender.asm.Name;

public class VanillaChunk implements IClassNodeTransformer {
	@Override
	public void transform(ClassNode cn) {
		MethodNode method = AsmUtil.findMethod(cn, Name.chunk_fillChunk, true);

		if (method == null)
			return; // the method doesn't exist on the server side

		method.localVariables = null;

		int part = 0;

		for (ListIterator<AbstractInsnNode> it = method.instructions.iterator(); it.hasNext();) {
			AbstractInsnNode insn = it.next();

			if (part == 0) { // find getBlockLSBArray() call, replace with setting the data
				if (insn.getOpcode() == INVOKEVIRTUAL) {
					if (Name.ebs_getBlockLSBArray.matches((MethodInsnNode) insn)) {
						// ExtendedBlockStorage is on the stack
						it.set(new VarInsnNode(ALOAD, 1)); // replace with data (byte[]) load
						it.add(new VarInsnNode(ILOAD, 6)); // offset (k)
						it.add(Name.hooks_setBlockData.staticInvocation());
						part++;
					}
				}
			}
			else if (part == 1) { // remove everything up to IADD (exclusive), insert add operands (k, Constants.ebsIdArraySize)
				if (insn.getOpcode() == IADD) {
					it.set(new VarInsnNode(ILOAD, 6));
					it.add(new LdcInsnNode(Constants.ebsIdArraySize));
					it.add(new InsnNode(IADD));
					part++;
				}
				else {
					it.remove();
				}
			}
			else if (part == 2) { // seek to ExtendedBlockStorage.getBlockMSBArray, then back to ICONST_0 or the preceding label
				if (insn.getOpcode() == INVOKEVIRTUAL) {
					if (Name.ebs_getBlockMSBArray.matches((MethodInsnNode) insn)) {
						while (it.previous().getOpcode() != ICONST_0);
						while (it.previous().getType() != AbstractInsnNode.LABEL);
						
						while (true) {
							insn = it.next();
							if (insn.getOpcode() == IF_ICMPLT || insn.getOpcode() == IF_ICMPGE) {
								break;
							}
						}
						it.set(new JumpInsnNode(insn.getOpcode() == IF_ICMPLT ? IF_ICMPGT : IF_ICMPLT, ((JumpInsnNode) insn).label));
						return;
					}
				}
			}
		}

		throw new AsmTransformException("no match for part " + part);
	}
}
