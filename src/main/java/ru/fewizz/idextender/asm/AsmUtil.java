package ru.fewizz.idextender.asm;

import java.io.PrintWriter;
import java.util.ListIterator;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceMethodVisitor;

public class AsmUtil {
	public static MethodNode findMethod(ClassNode cn, String name) {
		return findMethod(cn, name, false);
	}

	public static MethodNode findMethod(ClassNode cn, String name, boolean optional) {
		for (MethodNode ret : cn.methods) {
			if (ret.name.equals(name)) return ret;
		}

		if (optional) {
			return null;
		} else {
			throw new MethodNotFoundException(name);
		}
	}

	public static MethodNode findMethod(ClassNode cn, Name name) {
		return findMethod(cn, name, false);
	}

	public static MethodNode findMethod(ClassNode cn, Name name, boolean optional) {
		for (MethodNode ret : cn.methods) {
			if (name.matches(ret)) return ret;
		}

		if (optional) {
			return null;
		} else {
			throw new MethodNotFoundException(name.deobf);
		}
	}

	public static FieldNode findField(ClassNode cn, String name) {
		return findField(cn, name, false);
	}

	public static FieldNode findField(ClassNode cn, String name, boolean optional) {
		for (FieldNode ret : cn.fields) {
			if (name.equals(ret.name)) return ret;
		}

		if (optional) {
			return null;
		} else {
			throw new FieldNotFoundException(name);
		}
	}

	public static FieldNode findField(ClassNode cn, Name name) {
		return findField(cn, name, false);
	}

	public static FieldNode findField(ClassNode cn, Name name, boolean optional) {
		for (FieldNode ret : cn.fields) {
			if (name.matches(ret)) return ret;
		}

		if (optional) {
			return null;
		} else {
			throw new FieldNotFoundException(name.deobf);
		}
	}

	public static void makePublic(MethodNode x) {
		x.access = (x.access & ~(Opcodes.ACC_PRIVATE | Opcodes.ACC_PROTECTED)) | Opcodes.ACC_PUBLIC;
	}

	public static void makePublic(FieldNode x) {
		x.access = (x.access & ~(Opcodes.ACC_PRIVATE | Opcodes.ACC_PROTECTED)) | Opcodes.ACC_PUBLIC;
	}

	public static boolean transformInlinedSizeMethod(ClassNode cn, MethodNode method, int oldValue, int newValue, boolean optional) {
		boolean found = false;

		for (ListIterator<AbstractInsnNode> it = method.instructions.iterator(); it.hasNext();) {
			AbstractInsnNode insn = it.next();

			if (insn.getOpcode() == Opcodes.LDC) {
				LdcInsnNode node = (LdcInsnNode) insn;

				if (node.cst instanceof Integer &&
						(Integer) node.cst == oldValue) {
					node.cst = newValue;
					found = true;
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

					found = true;
				}
			}
		}

		if (!found && !optional) throw new AsmTransformException("can't find constant value "+oldValue+" in method "+method.name);

		return found;
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
