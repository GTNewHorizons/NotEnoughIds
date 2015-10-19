package ru.fewizz.idextender.asm;

import java.io.PrintWriter;
import java.util.ListIterator;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceMethodVisitor;

public class AsmUtil {
	public static MethodNode findMethod(ClassNode cn, String name) {
		return findMethod2(cn, name, null, null, null);
	}

	public static MethodNode findMethod(ClassNode cn, String name, String desc) {
		return findMethod2(cn, name, null, desc, null);
	}

	public static MethodNode findMethod2(ClassNode cn, String name, String altName) {
		return findMethod2(cn, name, altName, null, null);
	}

	public static MethodNode findMethod2(ClassNode cn, String name, String altName, String desc) {
		return findMethod2(cn, name, altName, desc, null);
	}

	public static MethodNode findMethod2(ClassNode cn, String name, String altName, String desc, String altDesc) {
		if (altDesc == null) altDesc = desc; // require desc for both if no new desc is supplied for altDesc

		for (MethodNode ret : cn.methods) {
			// check name+desc
			if (ret.name.equals(name) && (desc == null || ret.desc.equals(desc))) return ret;
			// check altName+altDesc
			if (ret.name.equals(altName) && (altDesc == null || ret.desc.equals(altDesc))) return ret;
		}

		return null;
	}

	public static MethodNode findMethod(ClassNode cn, Name name) {
		return findMethod2(cn, name.deobf, name.obf, name.desc, name.obfDesc);
	}

	public static boolean transformInlinedSizeMethod(ClassNode cn, MethodNode method, int oldValue, int newValue) {
		boolean ret = false;

		for (ListIterator<AbstractInsnNode> it = method.instructions.iterator(); it.hasNext();) {
			AbstractInsnNode insn = it.next();

			if (insn.getOpcode() == Opcodes.LDC) {
				LdcInsnNode node = (LdcInsnNode) insn;

				if (node.cst instanceof Integer &&
						(Integer) node.cst == oldValue) {
					node.cst = newValue;
					ret = true;
				}
			} else if (insn.getOpcode() == Opcodes.SIPUSH || insn.getOpcode() == Opcodes.BIPUSH) {
				IntInsnNode node = (IntInsnNode) insn;

				if (node.operand == oldValue) {
					if (newValue >= Byte.MIN_VALUE && newValue <= Byte.MAX_VALUE ||
							insn.getOpcode() == Opcodes.SIPUSH && newValue >= Short.MIN_VALUE && newValue <= Short.MAX_VALUE) {
						node.operand = newValue;
					} else {
						it.set(new LdcInsnNode(newValue));
					}

					ret = true;
				}
			}
		}

		return ret;
	}

	public static void dump(InsnList list) {
		Textifier textifier = new Textifier();
		TraceMethodVisitor visitor = new TraceMethodVisitor(textifier);
		list.accept(visitor);

		PrintWriter writer = new PrintWriter(System.out);
		textifier.print(writer);
		writer.flush();
	}
}
