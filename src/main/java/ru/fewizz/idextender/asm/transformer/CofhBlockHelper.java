package ru.fewizz.idextender.asm.transformer;

import java.util.ListIterator;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import ru.fewizz.idextender.asm.AsmUtil;
import ru.fewizz.idextender.asm.Constants;
import ru.fewizz.idextender.asm.FieldNotFoundException;
import ru.fewizz.idextender.asm.IClassNodeTransformer;
import ru.fewizz.idextender.asm.Name;

public class CofhBlockHelper implements IClassNodeTransformer {
	@Override
	public void transform(ClassNode cn, boolean obfuscated) {
		MethodNode method = AsmUtil.findMethod(cn, "<clinit>");
		if(!AsmUtil.transformInlinedSizeMethod(cn, method, 4096, Constants.maxBlockId + 1, true)){
			AsmUtil.transformInlinedSizeMethod(cn, method, 1024, Constants.maxBlockId + 1);
		}
	}
}
