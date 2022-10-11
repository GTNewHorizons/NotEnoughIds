package ru.fewizz.idextender.asm.transformer;

import org.objectweb.asm.tree.*;
import ru.fewizz.idextender.asm.*;

public class VanillaS26PacketMapChunkBulk implements IClassNodeTransformer {
    @Override
    public void transform(final ClassNode cn, final boolean obfuscated) {
        final MethodNode method = AsmUtil.findMethod(cn, Name.packet_readPacketData);
        AsmUtil.transformInlinedSizeMethod(cn, method, 8192, 12288, false);
    }
}
