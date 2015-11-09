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

import ru.fewizz.idextender.asm.AsmTransformException;
import ru.fewizz.idextender.asm.AsmUtil;
import ru.fewizz.idextender.asm.Constants;
import ru.fewizz.idextender.asm.IClassNodeTransformer;
import ru.fewizz.idextender.asm.IETransformer;
import ru.fewizz.idextender.asm.Name;

public class VanillaChunk implements IClassNodeTransformer {
	@Override
	public void transform(ClassNode cn, boolean obfuscated) {
		MethodNode method = AsmUtil.findMethod(cn, Name.chunk_fillChunk, !IETransformer.isClient());
		if (method == null) return; // the method doesn't exist on the server side

		method.localVariables = null;

		int part = 0;

		for (ListIterator<AbstractInsnNode> it = method.instructions.iterator(); it.hasNext();) {
			AbstractInsnNode insn = it.next();

			if (part == 0) { // find getBlockLSBArray() call, replace with setting the data
				if (insn.getOpcode() == Opcodes.INVOKEVIRTUAL) {
					if (Name.ebs_getBlockLSBArray.matches((MethodInsnNode)insn, obfuscated)) {
						// ExtendedBlockStorage is on the stack
						it.set(new VarInsnNode(Opcodes.ALOAD, 1)); // replace with data (byte[]) load
						it.add(new VarInsnNode(Opcodes.ILOAD, 6)); // offset (k)
						it.add(Name.hooks_setBlockData.staticInvocation(obfuscated));
						part++;
					}
				}
			} else if (part == 1) { // remove everything up to IADD (exclusive), insert add operands (k, Constants.ebsIdArraySize)
				if (insn.getOpcode() == Opcodes.IADD) {
					it.set(new VarInsnNode(Opcodes.ILOAD, 6));
					it.add(new LdcInsnNode(Constants.ebsIdArraySize));
					it.add(new InsnNode(Opcodes.IADD));
					part++;
				} 
				else 
					it.remove();
				
			} else if (part == 2) { // seek to ExtendedBlockStorage.getBlockMSBArray, then back to ICONST_0 or the preceding label
				if (insn.getOpcode() == Opcodes.INVOKEVIRTUAL) {
					MethodInsnNode node = (MethodInsnNode) insn;

					if (Name.ebs_getBlockMSBArray.matches(node, obfuscated)) {
						while (it.previous().getOpcode() != Opcodes.ICONST_0);
						while (it.previous().getType() != AbstractInsnNode.LABEL);

						it.next(); // reverse the iterator, returns the label again
						return;
					}
				}
			}
		}

		throw new AsmTransformException("no match for part "+part);
	}
}
