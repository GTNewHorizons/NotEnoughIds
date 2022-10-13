package ru.fewizz.idextender.asm.transformer;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import ru.fewizz.idextender.asm.AsmTransformException;
import ru.fewizz.idextender.asm.AsmUtil;
import ru.fewizz.idextender.asm.Constants;
import ru.fewizz.idextender.asm.IClassNodeTransformer;

public class VanillaBlockFire implements IClassNodeTransformer {
    @Override
    public void transform(ClassNode cn) {
        boolean found = false;

        for (MethodNode method : cn.methods) {
            if (AsmUtil.transformIntConst(cn, method, 4096, Constants.MAX_BLOCK_ID + 1, true)) found = true;
        }

        if (!found) throw new AsmTransformException("can't find 4096 constant in any method");
    }
}
