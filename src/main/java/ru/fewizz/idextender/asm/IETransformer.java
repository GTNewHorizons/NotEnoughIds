package ru.fewizz.idextender.asm;

import net.minecraft.launchwrapper.IClassTransformer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.util.CheckClassAdapter;

public class IETransformer implements IClassTransformer {
    private static final boolean PRE_VERIFICATION = false;
    private static final boolean POST_VERIFICATION = true;

    public static final Logger LOGGER = LogManager.getLogger("NEID");
    public static boolean isObfuscated;
    private static Boolean isClient = null;

    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes) {
        if (bytes == null) return bytes; // we don't create classes

        ClassEdit edit = ClassEdit.get(transformedName);
        if (edit == null) return bytes;

        LOGGER.debug("Patching {} with {}...", transformedName, edit.getName());

        // read
        ClassNode cn = new ClassNode(Opcodes.ASM5);
        ClassReader reader = new ClassReader(bytes);
        final int readFlags = 0;

        if (PRE_VERIFICATION) { // verify the code before processing it
            try {
                ClassVisitor check = new CheckClassAdapter(cn);
                reader.accept(check, readFlags);
            } catch (Throwable t) {
                LOGGER.error("Error preverifying {}: {}", transformedName, t.getMessage());
                throw new RuntimeException(t);
            }
        } else {
            reader.accept(cn, readFlags);
        }

        // patch

        try {
            edit.getTransformer().transform(cn);
        } catch (AsmTransformException t) {
            LOGGER.error("Error transforming {} with {}: {}", transformedName, edit.getName(), t.getMessage());
            throw t;
        } catch (Throwable t) {
            LOGGER.error("Error transforming {} with {}: {}", transformedName, edit.getName(), t.getMessage());
            throw new RuntimeException(t);
        }

        // write
        ClassWriter writer = new ClassWriter(0);

        if (POST_VERIFICATION) { // verify the code after processing it
            try {
                ClassVisitor check = new CheckClassAdapter(writer);
                cn.accept(check);
            } catch (Throwable t) {
                LOGGER.error(
                        "Error verifying {} transformed with {}: {}", transformedName, edit.getName(), t.getMessage());

                throw new RuntimeException(t);
            }
        } else {
            cn.accept(writer);
        }

        LOGGER.debug("Patched {} successfully.", edit.getName());

        return writer.toByteArray();
    }

    public static boolean isClient() {
        if (isClient == null)
            isClient = IETransformer.class.getResource("/net/minecraft/client/main/Main.class") != null;

        return isClient;
    }
}
