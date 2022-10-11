package ru.fewizz.idextender.asm.transformer;

import ru.fewizz.idextender.asm.*;
import org.objectweb.asm.tree.*;

public class VanillaPlayerControllerMP implements IClassNodeTransformer
{
    @Override
    public void transform(final ClassNode cn, final boolean obfuscated) {
        final MethodNode method = AsmUtil.findMethod(cn, Name.playerControllerMP_onPlayerDestroyBlock);
        AsmUtil.transformInlinedSizeMethod(cn, method, 12, 16);
    }
}
