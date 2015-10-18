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
	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes) {
		ClassEdit edit = ClassEdit.get(transformedName);
		if (edit == null) return bytes;

		logger.debug("Patching {}...", edit.getName());

		// read
		ClassNode cn = new ClassNode(Opcodes.ASM5);
		ClassReader reader = new ClassReader(bytes);
		final int readFlags = 0;

		if (enablePreVerification) { // verify the code before processing it
			try {
				ClassVisitor check = new CheckClassAdapter(cn);
				reader.accept(check, readFlags);
			} catch (Throwable t) {
				logger.error("Error preverifying {}: {}", transformedName, t.getMessage());

				throw new RuntimeException(t);
			}
		} else {
			reader.accept(cn, readFlags);
		}

		// patch
		boolean result;

		try {
			result = edit.getTransformer().transform(cn, isObfuscated());
		} catch (Throwable t) {
			logger.error("Error transforming {} with {}: {}", transformedName, edit.getName(), t.getMessage());

			throw new RuntimeException(t);
		}

		// write
		ClassWriter writer = new ClassWriter(0);

		if (enablePostVerification) { // verify the code after processing it
			try {
				ClassVisitor check = new CheckClassAdapter(writer);
				cn.accept(check);
			} catch (Throwable t) {
				logger.error("Error verifying {} transformed with {}: {}", transformedName, edit.getName(), t.getMessage());

				throw new RuntimeException(t);
			}
		} else {
			cn.accept(writer);
		}

		if (result) {
			logger.debug("Patched {} successfully.", edit.getName());
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

	private static final boolean enablePreVerification = false;
	private static final boolean enablePostVerification = true;

	public static Logger logger = LogManager.getLogger("NEID");
	private static Boolean isObfuscated = null;
}
