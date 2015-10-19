package ru.fewizz.idextender.asm.transformer;

import java.util.ListIterator;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;
import ru.fewizz.idextender.asm.Constants;
import ru.fewizz.idextender.asm.IClassNodeTransformer;

public class FmlRegistry implements IClassNodeTransformer {
	@Override
	public boolean transform(ClassNode cn, boolean obfuscated) {
		for (FieldNode field : cn.fields) {
			if (field.name.equals("MAX_BLOCK_ID")) {
				field.value = Constants.maxBlockId;
				break;
			}
		}

		for (MethodNode method : cn.methods) {
			InsnList code = method.instructions;

			for (ListIterator<AbstractInsnNode> iterator = code.iterator(); iterator.hasNext();) {
				AbstractInsnNode insn = iterator.next();

				if (insn.getType() == AbstractInsnNode.INT_INSN && ((IntInsnNode) insn).operand == 4095) {
					iterator.set(new LdcInsnNode(Constants.maxBlockId));
				}
			}
		}

		return true;
	}
}
