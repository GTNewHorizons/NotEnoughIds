package ru.fewizz.idextender.asm.transformer;

import org.objectweb.asm.tree.*;
import ru.fewizz.idextender.*;
import ru.fewizz.idextender.asm.*;

public class MFQM implements IClassNodeTransformer {
    @Override
    public void transform(final ClassNode cn, final boolean obfuscated) {
        if (IEConfig.extendDataWatcher) {
            final MethodNode method = AsmUtil.findMethod(cn, Name.MFQM_preInit);
            AsmUtil.transformInlinedSizeMethod(cn, method, 31, 127);
        }
    }
}
