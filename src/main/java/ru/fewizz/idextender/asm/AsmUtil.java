package ru.fewizz.idextender.asm;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

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
		for (MethodNode ret : cn.methods) {
			if ((ret.name.equals(name) || ret.name.equals(altName)) &&
					(desc == null || ret.desc.equals(desc) || ret.desc.equals(altDesc))) { // desc == null -> no desc check
				return ret;
			}
		}

		return null;
	}
}
