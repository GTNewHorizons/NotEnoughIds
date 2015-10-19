package ru.fewizz.idextender.asm.transformer;

import java.util.ListIterator;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import ru.fewizz.idextender.asm.AsmUtil;
import ru.fewizz.idextender.asm.Constants;
import ru.fewizz.idextender.asm.IClassNodeTransformer;
import ru.fewizz.idextender.asm.Name;

public class VanillaNetHandlerPlayClient implements IClassNodeTransformer {
	@Override
	public boolean transform(ClassNode cn, boolean obfuscated) {
		MethodNode method = AsmUtil.findMethod(cn, Name.nhpc_handleMultiBlockChange);
		if (method == null) return false;

		InsnList code = method.instructions;
		int part = 0;

		for (ListIterator<AbstractInsnNode> iterator = code.iterator(); iterator.hasNext();) {
			AbstractInsnNode insn = iterator.next();

			if (part == 0) { // seek to ISTORE 7, prefix with masking
				if (insn.getOpcode() == Opcodes.ISTORE && ((VarInsnNode) insn).var == 7) {
					iterator.set(new LdcInsnNode(Constants.blockIdMask));
					iterator.add(new InsnNode(Opcodes.IAND));
					iterator.add(new VarInsnNode(Opcodes.ISTORE, 7));
					part++;
				}
			} else if (part == 1) { // seek to ILOAD 7, replace with DataInputStream.read()
				if (insn.getOpcode() == Opcodes.ILOAD && ((VarInsnNode) insn).var == 7) {
					iterator.set(new VarInsnNode(Opcodes.ALOAD, 4)); // DataInputStream
					iterator.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/io/DataInputStream", "read", "()I", false));
					part++;
				}
			} else { // remove everything up to ISTORE (exclusive)
				if (insn.getOpcode() == Opcodes.ISTORE) {
					return true;
				} else {
					iterator.remove();
				}
			}
		}

		return false;
	}
}
