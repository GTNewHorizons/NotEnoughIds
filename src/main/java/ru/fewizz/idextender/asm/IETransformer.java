package ru.fewizz.idextender.asm;

import net.minecraft.launchwrapper.IClassTransformer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;


public class IETransformer implements IClassTransformer {
	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes) {
		ClassEdit edit = ClassEdit.get(transformedName);
		if (edit == null) return bytes;

		logger.info("Patching {}...", edit.getName());

		// read
		ClassNode cn = new ClassNode(Opcodes.ASM5);
		ClassReader reader = new ClassReader(bytes);

		reader.accept(cn, ClassReader.SKIP_FRAMES);

		// patch
		boolean result;

		try {
			result = edit.getTransformer().transform(cn, isObfuscated());
		} catch (Throwable t) {
			logger.error("Error transforming "+transformedName+" with "+edit.getName(), t);

			throw new RuntimeException(t);
		}

		// write
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		cn.accept(writer);

		if (result) {
			logger.info("Patched {} successfully.", edit.getName());
		} else {
			logger.warn("Patching {} failed.", edit.getName());
		}

		return writer.toByteArray();
	}

	private static boolean isObfuscated() {
		if (isObfuscated == null) {
			isObfuscated = IETransformer.class.getResource("/net/minecraft/world/World.class") == null;
		}

		return isObfuscated;
	}

	public static Logger logger = LogManager.getLogger("NEID");
	private static Boolean isObfuscated = null;
}
