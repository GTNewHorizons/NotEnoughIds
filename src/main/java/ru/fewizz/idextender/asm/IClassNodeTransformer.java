package ru.fewizz.idextender.asm;

import org.objectweb.asm.tree.ClassNode;

public interface IClassNodeTransformer {
	boolean transform(ClassNode cn, boolean obfuscated);
}
