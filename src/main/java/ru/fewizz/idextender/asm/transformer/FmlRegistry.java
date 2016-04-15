package ru.fewizz.idextender.asm.transformer;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;
import ru.fewizz.idextender.asm.AsmTransformException;
import ru.fewizz.idextender.asm.AsmUtil;
import ru.fewizz.idextender.asm.Constants;
import ru.fewizz.idextender.asm.IClassNodeTransformer;

public class FmlRegistry implements IClassNodeTransformer {
	@Override
	public void transform(ClassNode cn, boolean obfuscated) {
		FieldNode field = AsmUtil.findField(cn, "MAX_BLOCK_ID", true);
		if (field != null) {
			field.value = Constants.maxBlockId;
		}

		boolean found = false;

		for (MethodNode method : cn.methods) {
			if (AsmUtil.transformInlinedSizeMethod(cn, method, 4095, Constants.maxBlockId, true)) {
				found = true;
			}
		}

		if (!found)
			throw new AsmTransformException("can't find 4095 constant in any method");
	}
}
