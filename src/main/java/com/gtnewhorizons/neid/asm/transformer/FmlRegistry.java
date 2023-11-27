package com.gtnewhorizons.neid.asm.transformer;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import com.gtnewhorizons.neid.Constants;
import com.gtnewhorizons.neid.asm.AsmTransformException;
import com.gtnewhorizons.neid.asm.AsmUtil;
import com.gtnewhorizons.neid.asm.IClassNodeTransformer;

public class FmlRegistry implements IClassNodeTransformer {

    @Override
    public void transform(final ClassNode cn, final boolean obfuscated) {
        final FieldNode field = AsmUtil.findField(cn, "MAX_BLOCK_ID", true);
        if (field != null) {
            field.value = 32767;
        }
        boolean found = false;
        for (final MethodNode method : cn.methods) {
            if (AsmUtil.transformInlinedSizeMethod(
                    cn,
                    method,
                    Constants.VANILLA_MAX_BLOCK_ID,
                    Constants.MAX_BLOCK_ID,
                    true)) {
                found = true;
            }
        }
        if (!found) {
            throw new AsmTransformException("can't find 4095 constant in any method");
        }
    }
}
