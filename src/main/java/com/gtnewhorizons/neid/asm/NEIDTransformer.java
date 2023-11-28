package com.gtnewhorizons.neid.asm;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.Launch;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.util.CheckClassAdapter;

import com.gtnewhorizons.neid.core.NEIDCore;

public class NEIDTransformer implements IClassTransformer {

    private static final boolean DUMP_CLASSES = Boolean.parseBoolean(System.getProperty("neid.dumpClass", "false"));
    private static final Logger logger = LogManager.getLogger("NEID");

    public byte[] transform(final String name, final String transformedName, final byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        final ClassNodeTransformers transformer = ClassNodeTransformers.get(transformedName);
        if (transformer == null) {
            return bytes;
        }
        NEIDTransformer.logger.debug("Patching {} with {}...", transformedName, transformer.getName());
        final ClassNode cn = new ClassNode(Opcodes.ASM5);
        final ClassReader cr = new ClassReader(bytes);
        cr.accept(cn, ClassReader.EXPAND_FRAMES);
        try {
            transformer.transform(cn, NEIDCore.isObfuscated());
        } catch (AsmTransformException t) {
            NEIDTransformer.logger
                    .error("Error transforming {} with {}: {}", transformedName, transformer.getName(), t.getMessage());
            throw t;
        } catch (Throwable t2) {
            NEIDTransformer.logger.error(
                    "Error transforming {} with {}: {}",
                    transformedName,
                    transformer.getName(),
                    t2.getMessage());
            throw new RuntimeException(t2);
        }
        final ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        try {
            final ClassVisitor check = new CheckClassAdapter(cw);
            cn.accept(check);
        } catch (Throwable t3) {
            NEIDTransformer.logger.error(
                    "Error verifying {} transformed with {}: {}",
                    transformedName,
                    transformer.getName(),
                    t3.getMessage());
            throw new RuntimeException(t3);
        }
        NEIDTransformer.logger.debug("Patched {} successfully.", transformer.getName());
        final byte[] transformedBytes = cw.toByteArray();
        saveTransformedClass(transformedBytes, transformedName);
        return transformedBytes;
    }

    private File outputDir = null;

    private void saveTransformedClass(final byte[] data, final String transformedName) {
        if (!DUMP_CLASSES) {
            return;
        }
        if (outputDir == null) {
            outputDir = new File(Launch.minecraftHome, "ASM_NEID");
            try {
                FileUtils.deleteDirectory(outputDir);
            } catch (IOException ignored) {}
            if (!outputDir.exists()) {
                // noinspection ResultOfMethodCallIgnored
                outputDir.mkdirs();
            }
        }
        final String fileName = transformedName.replace('.', File.separatorChar);
        final File classFile = new File(outputDir, fileName + ".class");
        final File outDir = classFile.getParentFile();
        if (!outDir.exists()) {
            // noinspection ResultOfMethodCallIgnored
            outDir.mkdirs();
        }
        if (classFile.exists()) {
            // noinspection ResultOfMethodCallIgnored
            classFile.delete();
        }
        try (final OutputStream output = Files.newOutputStream(classFile.toPath())) {
            output.write(data);
        } catch (IOException e) {
            logger.error("Could not save transformed class (byte[]) " + transformedName, e);
        }
    }

}
