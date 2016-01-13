package ru.fewizz.idextender.asm.transformer;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import ru.fewizz.idextender.IEConfig;
import ru.fewizz.idextender.asm.AsmUtil;
import ru.fewizz.idextender.asm.Constants;
import ru.fewizz.idextender.asm.IClassNodeTransformer;
import ru.fewizz.idextender.asm.Name;

public class MFQM implements IClassNodeTransformer {

	@Override
	public void transform(ClassNode cn, boolean obfuscated) {
		if (IEConfig.extendDataWatcher) {
			MethodNode method = AsmUtil.findMethod(cn, Name.MFQM_preInit);
			AsmUtil.transformInlinedSizeMethod(cn, method, 31, Constants.maxDataWatcherId);
		}
	}

}
