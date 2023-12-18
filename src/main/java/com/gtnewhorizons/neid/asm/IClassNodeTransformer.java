package com.gtnewhorizons.neid.asm;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

public interface IClassNodeTransformer extends Opcodes {

    /**
     * Returns the de-obfuscated names of the classes targeted by this transformer Example :
     * "net.minecraft.client.Minecraft"
     */
    String[] getTargetClass();

    /**
     * Performs the transformations on the ClassNode
     */
    void transform(final ClassNode cn, final boolean obfuscated);
}
