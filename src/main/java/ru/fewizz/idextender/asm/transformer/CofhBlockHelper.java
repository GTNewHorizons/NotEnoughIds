package ru.fewizz.idextender.asm.transformer;

import java.util.ListIterator;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;
import ru.fewizz.idextender.asm.AsmUtil;
import ru.fewizz.idextender.asm.Constants;
import ru.fewizz.idextender.asm.IClassNodeTransformer;

public class CofhBlockHelper implements IClassNodeTransformer {
	@Override
	public boolean transform(ClassNode cn, boolean obfuscated) {
		int oldValue = -1;

		for (FieldNode field : cn.fields) {
			if (field.name.equals("MAX_ID")) {
				oldValue = (Integer) field.value;
				field.value = Constants.maxBlockId + 1;
				break;
			}
		}

		if (oldValue == -1) return false;

		// set the inlined use of MAX_ID to the new value as well
		MethodNode method = AsmUtil.findMethod(cn, "<clinit>");
		if (method == null) return false;

		InsnList code = method.instructions;

		for (ListIterator<AbstractInsnNode> iterator = code.iterator(); iterator.hasNext();) {
			AbstractInsnNode insn = iterator.next();

			if (insn.getType() == AbstractInsnNode.INT_INSN && ((IntInsnNode) insn).operand == oldValue) {
				iterator.set(new LdcInsnNode(Constants.maxBlockId + 1));

				return true;
			}
		}

		return false;
	}
}
