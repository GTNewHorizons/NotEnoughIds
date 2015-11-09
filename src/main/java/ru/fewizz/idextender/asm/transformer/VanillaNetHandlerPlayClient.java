package ru.fewizz.idextender.asm.transformer;

import java.util.ListIterator;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import ru.fewizz.idextender.asm.AsmTransformException;
import ru.fewizz.idextender.asm.AsmUtil;
import ru.fewizz.idextender.asm.Constants;
import ru.fewizz.idextender.asm.IClassNodeTransformer;
import ru.fewizz.idextender.asm.Name;

public class VanillaNetHandlerPlayClient implements IClassNodeTransformer {
	@Override
	public void transform(ClassNode cn, boolean obfuscated) {
		MethodNode method = AsmUtil.findMethod(cn, Name.nhpc_handleMultiBlockChange);

		InsnList code = method.instructions;
		int part = 0;

		for (ListIterator<AbstractInsnNode> iterator = code.iterator(); iterator.hasNext();) {
			AbstractInsnNode insn = iterator.next();
			
			if (part == 0) { // short short1
				if (insn.getOpcode() == Opcodes.ALOAD && ((VarInsnNode) insn).var == 4)
					part++;
			}
			else if (part == 1) { // short short2
				if (insn.getOpcode() == Opcodes.ALOAD && ((VarInsnNode) insn).var == 4) {
					iterator.remove();
					insn = iterator.next();
					iterator.set(new InsnNode(Opcodes.ICONST_0));
					part++;
				}
			}
			else if (part == 2){ // int l
				if (insn.getOpcode() == Opcodes.ILOAD && ((VarInsnNode) insn).var == 7){
					iterator.set(new VarInsnNode(Opcodes.ALOAD, 4));
					iterator.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/io/DataInputStream", "readShort", "()S", false));
					part++;
				}
			}
			else if (part == 3){ // remove everything up to ISTORE (exclusive)
				if (insn.getOpcode() == Opcodes.ISTORE) {
					part++;
				} else {
					iterator.remove();
				}
			}
			else if (part == 4){ // int i1
				if (insn.getOpcode() == Opcodes.ILOAD && ((VarInsnNode) insn).var == 7){
					iterator.set(new VarInsnNode(Opcodes.ALOAD, 4));
					iterator.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/io/DataInputStream", "readByte", "()B", false));
					iterator.add(new IntInsnNode(Opcodes.BIPUSH, 15));
					iterator.add(new InsnNode(Opcodes.IAND));
					part++;
				}
			}
			else if (part == 5){ // remove everything up to ISTORE (exclusive)
				if (insn.getOpcode() == Opcodes.ISTORE) {
					return;
				} else {
					iterator.remove();
				}
			}
		}

		throw new AsmTransformException("no match for part "+part);
	}
}
