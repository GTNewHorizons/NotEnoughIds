package ru.fewizz.idextender.asm.transformer;

import org.objectweb.asm.tree.ClassNode;
import ru.fewizz.idextender.asm.AsmUtil;
import ru.fewizz.idextender.asm.Constants;
import ru.fewizz.idextender.asm.IClassNodeTransformer;

public class UndergroundBiomesOreUBifier implements IClassNodeTransformer {
    @Override
    public void transform(ClassNode cn) {
        ;
        AsmUtil.transformIntConst(cn, "renewBlockReplacers", 4096, Constants.MAX_BLOCK_ID + 1);
    }
}
