package ru.fewizz.idextender.asm.transformer;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import ru.fewizz.idextender.asm.AsmUtil;
import ru.fewizz.idextender.asm.IClassNodeTransformer;
import ru.fewizz.idextender.asm.Name;
import scala.tools.nsc.backend.jvm.AsmUtils;

public class VanillaPlayerControllerMP implements IClassNodeTransformer {

	@Override
	public void transform(ClassNode cn, boolean obfuscated) {
		MethodNode method = AsmUtil.findMethod(cn, Name.playerControllerMP_onPlayerDestroyBlock);
		AsmUtil.transformInlinedSizeMethod(cn, method, 12, 16);
	}

}
