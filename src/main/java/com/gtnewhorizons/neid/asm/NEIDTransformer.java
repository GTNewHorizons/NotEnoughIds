package com.gtnewhorizons.neid.asm;

import net.minecraft.launchwrapper.IClassTransformer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.util.CheckClassAdapter;

public class NEIDTransformer implements IClassTransformer {

    private static final boolean enablePreVerification = false;
    private static final boolean enablePostVerification = true;
    public static final Logger logger;
    public static boolean isObfuscated;
    private static Boolean isClient;

    public byte[] transform(final String name, final String transformedName, final byte[] bytes) {
        if (bytes == null) {
            return bytes;
        }
        final ClassEdit edit = ClassEdit.get(transformedName);
        if (edit == null) {
            return bytes;
        }
        NEIDTransformer.logger.debug("Patching {} with {}...", transformedName, edit.getName());
        final ClassNode cn = new ClassNode(Opcodes.ASM5);
        final ClassReader reader = new ClassReader(bytes);
        final int readFlags = 0;
        reader.accept(cn, ClassReader.EXPAND_FRAMES);
        try {
            edit.getTransformer().transform(cn, NEIDTransformer.isObfuscated);
        } catch (AsmTransformException t) {
            NEIDTransformer.logger
                    .error("Error transforming {} with {}: {}", transformedName, edit.getName(), t.getMessage());
            throw t;
        } catch (Throwable t2) {
            NEIDTransformer.logger
                    .error("Error transforming {} with {}: {}", transformedName, edit.getName(), t2.getMessage());
            throw new RuntimeException(t2);
        }
        final ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        try {
            final ClassVisitor check = new CheckClassAdapter(writer);
            cn.accept(check);
        } catch (Throwable t3) {
            NEIDTransformer.logger.error(
                    "Error verifying {} transformed with {}: {}",
                    transformedName,
                    edit.getName(),
                    t3.getMessage());
            throw new RuntimeException(t3);
        }
        NEIDTransformer.logger.debug("Patched {} successfully.", edit.getName());
        return writer.toByteArray();
    }

    public static boolean isClient() {
        if (NEIDTransformer.isClient == null) {
            NEIDTransformer.isClient = (NEIDTransformer.class.getResource("/net/minecraft/client/main/Main.class")
                    != null);
        }
        return NEIDTransformer.isClient;
    }

    static {
        logger = LogManager.getLogger("NEID");
        NEIDTransformer.isClient = null;
    }
}