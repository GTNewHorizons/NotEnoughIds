package ru.fewizz.idextender.asm.transformer;

import org.objectweb.asm.tree.ClassNode;
import ru.fewizz.idextender.IEConfig;
import ru.fewizz.idextender.asm.AsmUtil;
import ru.fewizz.idextender.asm.Constants;
import ru.fewizz.idextender.asm.IClassNodeTransformer;
import ru.fewizz.idextender.asm.Name;

public class MFQM implements IClassNodeTransformer {

    @Override
    public void transform(ClassNode cn) {
        if (IEConfig.extendDataWatcher)
            AsmUtil.transformIntConst(cn, Name.MFQM_preInit, 31, Constants.maxDataWatcherId);
    }
}
