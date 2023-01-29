package ru.fewizz.idextender.asm.transformer;

import org.objectweb.asm.tree.*;

import ru.fewizz.idextender.asm.*;

public class VanillaPlayerControllerMP implements IClassNodeTransformer {

    @Override
    public void transform(final ClassNode cn, final boolean obfuscated) {
        final MethodNode method = AsmUtil.findMethod(cn, Name.playerControllerMP_onPlayerDestroyBlock);
        AsmUtil.transformInlinedSizeMethod(cn, method, 12, 16);
    }
}
