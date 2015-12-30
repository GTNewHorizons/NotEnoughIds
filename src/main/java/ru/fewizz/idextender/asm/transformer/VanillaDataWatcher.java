package ru.fewizz.idextender.asm.transformer;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import ru.fewizz.idextender.IEConfig;
import ru.fewizz.idextender.asm.AsmUtil;
import ru.fewizz.idextender.asm.IClassNodeTransformer;
import ru.fewizz.idextender.asm.Name;
import scala.tools.nsc.backend.jvm.AsmUtils;

public class VanillaDataWatcher implements IClassNodeTransformer{

	@Override
	public void transform(ClassNode cn, boolean obfuscated) {
		if(IEConfig.extendDataWatcher){
			MethodNode method = AsmUtil.findMethod(cn, Name.dataWatcher_addObject);
			AsmUtil.transformInlinedSizeMethod(cn, method, 31, 63, false);
		}
	}
}
