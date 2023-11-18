package com.gtnewhorizons.neid.asm.transformer;

import java.util.ListIterator;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import com.gtnewhorizons.neid.NEIDConfig;
import com.gtnewhorizons.neid.asm.AsmUtil;
import com.gtnewhorizons.neid.asm.IClassNodeTransformer;
import com.gtnewhorizons.neid.asm.Name;

public class VanillaDataWatcher implements IClassNodeTransformer {

    @Override
    public void transform(final ClassNode cn, final boolean obfuscated) {
        if (NEIDConfig.extendDataWatcher) {
            MethodNode method = AsmUtil.findMethod(cn, Name.dataWatcher_addObject);
            AsmUtil.transformInlinedSizeMethod(cn, method, 31, 127);
            method = AsmUtil.findMethod(cn, Name.dataWatcher_writeWatchableObjectToPacketBuffer);
            AsmUtil.transformInlinedSizeMethod(cn, method, 31, 127);
            AsmUtil.transformInlinedSizeMethod(cn, method, 255, 1023);
            AsmUtil.transformInlinedSizeMethod(cn, method, 5, 7);
            final ListIterator<AbstractInsnNode> it = method.instructions.iterator();
            while (it.hasNext()) {
                final AbstractInsnNode insn = it.next();
                if (insn.getOpcode() == 182 && ((MethodInsnNode) insn).name.equals("writeByte")) {
                    it.set(
                            new MethodInsnNode(
                                    182,
                                    "net/minecraft/network/PacketBuffer",
                                    "writeShort",
                                    "(I)Lio/netty/buffer/ByteBuf;",
                                    false));
                    break;
                }
            }
            method = AsmUtil.findMethod(cn, Name.dataWatcher_readWatchedListFromPacketBuffer);
            AsmUtil.transformInlinedSizeMethod(cn, method, 127, 32767);
            AsmUtil.transformInlinedSizeMethod(cn, method, 224, 896);
            AsmUtil.transformInlinedSizeMethod(cn, method, 31, 127);
            AsmUtil.transformInlinedSizeMethod(cn, method, 5, 7);
            int num = 0;
            ListIterator<AbstractInsnNode> it2 = method.instructions.iterator();
            while (it2.hasNext()) {
                final AbstractInsnNode insn2 = it2.next();
                if (insn2.getOpcode() == 182 && ((MethodInsnNode) insn2).name.equals("readByte")) {
                    if (++num == 2) {
                        continue;
                    }
                    it2.set(new MethodInsnNode(182, "net/minecraft/network/PacketBuffer", "readShort", "()S", false));
                    if (num == 3) {
                        break;
                    }
                    continue;
                }
            }
            method = AsmUtil.findMethod(cn, Name.dataWatcher_writeWatchedListToPacketBuffer);
            AsmUtil.transformInlinedSizeMethod(cn, method, 127, 32767);
            it2 = method.instructions.iterator();
            while (it2.hasNext()) {
                final AbstractInsnNode insn2 = it2.next();
                if (insn2.getOpcode() == 182 && ((MethodInsnNode) insn2).name.equals("writeByte")) {
                    it2.set(
                            new MethodInsnNode(
                                    182,
                                    "net/minecraft/network/PacketBuffer",
                                    "writeShort",
                                    "(I)Lio/netty/buffer/ByteBuf;",
                                    false));
                    break;
                }
            }
            method = AsmUtil.findMethod(cn, Name.dataWatcher_func_151509_a);
            AsmUtil.transformInlinedSizeMethod(cn, method, 127, 32767);
            it2 = method.instructions.iterator();
            while (it2.hasNext()) {
                final AbstractInsnNode insn2 = it2.next();
                if (insn2.getOpcode() == 182 && ((MethodInsnNode) insn2).name.equals("writeByte")) {
                    it2.set(
                            new MethodInsnNode(
                                    182,
                                    "net/minecraft/network/PacketBuffer",
                                    "writeShort",
                                    "(I)Lio/netty/buffer/ByteBuf;",
                                    false));
                    break;
                }
            }
        }
    }
}
