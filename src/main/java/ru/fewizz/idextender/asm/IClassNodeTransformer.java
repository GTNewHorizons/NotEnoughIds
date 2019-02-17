package ru.fewizz.idextender.asm;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

public interface IClassNodeTransformer extends Opcodes {
	void transform(ClassNode cn);
}
