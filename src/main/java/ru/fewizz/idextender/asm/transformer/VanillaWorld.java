package ru.fewizz.idextender.asm.transformer;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import ru.fewizz.idextender.asm.AsmUtil;
import ru.fewizz.idextender.asm.IClassNodeTransformer;
import ru.fewizz.idextender.asm.Name;

public class VanillaWorld implements IClassNodeTransformer {

	@Override
	public void transform(ClassNode cn, boolean obfuscated) {
		MethodNode method = AsmUtil.findMethod(cn, Name.world_breakBlock);
		AsmUtil.transformInlinedSizeMethod(cn, method, 12, 16);
	}

}
