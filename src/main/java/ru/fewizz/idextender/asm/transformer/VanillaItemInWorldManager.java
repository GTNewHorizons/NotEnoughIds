package ru.fewizz.idextender.asm.transformer;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import ru.fewizz.idextender.asm.AsmUtil;
import ru.fewizz.idextender.asm.IClassNodeTransformer;
import ru.fewizz.idextender.asm.Name;

public class VanillaItemInWorldManager implements IClassNodeTransformer {

	@Override
	public void transform(ClassNode cn, boolean obfuscated) {
		MethodNode method = AsmUtil.findMethod(cn, Name.itemInWorldManager_tryHarvestBlock);
		AsmUtil.transformInlinedSizeMethod(cn, method, 12, 16);
	}
	
}
