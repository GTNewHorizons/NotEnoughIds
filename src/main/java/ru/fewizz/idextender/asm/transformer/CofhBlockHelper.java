package ru.fewizz.idextender.asm.transformer;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;
import ru.fewizz.idextender.asm.AsmUtil;
import ru.fewizz.idextender.asm.Constants;
import ru.fewizz.idextender.asm.IClassNodeTransformer;

public class CofhBlockHelper implements IClassNodeTransformer {
	@Override
	public void transform(ClassNode cn, boolean obfuscated) {
		// set the inlined use of MAX_ID to the new value as well
		MethodNode method = AsmUtil.findMethod(cn, "<clinit>");
		AsmUtil.transformInlinedSizeMethod(cn, method, 4096, Constants.maxBlockId + 1, false);
	}
}
