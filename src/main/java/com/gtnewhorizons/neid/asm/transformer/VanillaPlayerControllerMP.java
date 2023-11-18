package com.gtnewhorizons.neid.asm.transformer;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import com.gtnewhorizons.neid.asm.AsmUtil;
import com.gtnewhorizons.neid.asm.IClassNodeTransformer;
import com.gtnewhorizons.neid.asm.Name;

public class VanillaPlayerControllerMP implements IClassNodeTransformer {

    @Override
    public void transform(final ClassNode cn, final boolean obfuscated) {
        final MethodNode method = AsmUtil.findMethod(cn, Name.playerControllerMP_onPlayerDestroyBlock);
        AsmUtil.transformInlinedSizeMethod(cn, method, 12, 16);
    }
}
