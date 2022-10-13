package ru.fewizz.idextender.asm.transformer;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import ru.fewizz.idextender.asm.AsmUtil;
import ru.fewizz.idextender.asm.Constants;
import ru.fewizz.idextender.asm.IClassNodeTransformer;

public class CofhBlockHelper implements IClassNodeTransformer {
    @Override
    public void transform(ClassNode cn) {
        MethodNode method = AsmUtil.findMethod(cn, "<clinit>");
        if (!AsmUtil.transformIntConst(cn, method, 4096, Constants.MAX_BLOCK_ID + 1, true)) {
            AsmUtil.transformIntConst(cn, method, 1024, Constants.MAX_BLOCK_ID + 1);
        }
    }
}
