package ru.fewizz.idextender.asm.transformer;

import ru.fewizz.idextender.asm.*;
import org.objectweb.asm.tree.*;

public class VanillaS24PacketBlockActivation implements IClassNodeTransformer
{
    @Override
    public void transform(final ClassNode cn, final boolean obfuscated) {
        MethodNode method = AsmUtil.findMethod(cn, Name.packet_readPacketData);
        AsmUtil.transformInlinedSizeMethod(cn, method, 4095, 65535);
        method = AsmUtil.findMethod(cn, Name.packet_writePacketData);
        AsmUtil.transformInlinedSizeMethod(cn, method, 4095, 65535);
    }
}
