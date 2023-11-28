package com.gtnewhorizons.neid.asm.transformer;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import com.gtnewhorizons.neid.NEIDConfig;
import com.gtnewhorizons.neid.asm.AsmUtil;
import com.gtnewhorizons.neid.asm.IClassNodeTransformer;
import com.gtnewhorizons.neid.asm.Name;

public class MFQM implements IClassNodeTransformer {

    @Override
    public String[] getTargetClass() {
        return new String[] { "MoreFunQuicksandMod.main.MFQM" };
    }

    @Override
    public void transform(final ClassNode cn, final boolean obfuscated) {
        if (NEIDConfig.extendDataWatcher) {
            final MethodNode method = AsmUtil.findMethod(cn, Name.MFQM_preInit);
            AsmUtil.transformInlinedSizeMethod(method, 31, 127);
        }
    }
}
