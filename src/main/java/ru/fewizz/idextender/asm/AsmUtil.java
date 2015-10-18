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
}
