package ru.fewizz.idextender.asm.transformer;

import java.util.ListIterator;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import ru.fewizz.idextender.asm.AsmUtil;
import ru.fewizz.idextender.asm.IClassNodeTransformer;
import ru.fewizz.idextender.asm.Name;

public class UndergroundBiomesBiomeUndergroundDecorator implements IClassNodeTransformer {
	@Override
	public boolean transform(ClassNode cn, boolean obfuscated) {
		MethodNode method = AsmUtil.findMethod(cn, Name.ub_bud_replaceChunkOres_world);
		if (method == null || !transformReplaceChunkOres(cn, method, obfuscated, 1)) return false;

		method = AsmUtil.findMethod(cn, Name.ub_bud_replaceChunkOres_iChunkProvider);
		if (method == null || !transformReplaceChunkOres(cn, method, obfuscated, 0)) return false;

		return true;
	}

	private boolean transformReplaceChunkOres(ClassNode cn, MethodNode method, boolean obfuscated, int varOffset) {
		InsnList code = method.instructions;
		int part = 0;

		for (ListIterator<AbstractInsnNode> iterator = code.iterator(); iterator.hasNext();) {
			AbstractInsnNode insn = iterator.next();

			if (part == 0) { // find ExtendedBlockStorage.getBlockLSBArray, replace with Hooks.getBlockId
				if (insn.getOpcode() == Opcodes.INVOKEVIRTUAL &&
						((MethodInsnNode) insn).name.equals(Name.ebs_getBlockLSBArray.get(obfuscated))) {
					iterator.remove();
					// ExtendedBlockStorage is on the stack
					iterator.add(new VarInsnNode(Opcodes.ILOAD, 9 + varOffset)); // x
					iterator.add(new VarInsnNode(Opcodes.ILOAD, 18 + varOffset)); // y
					iterator.add(new VarInsnNode(Opcodes.ILOAD, 10 + varOffset)); // z
					iterator.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
							Name.hooks.get(obfuscated),
							Name.hooks_getBlockId.get(obfuscated),
							Name.hooks_getBlockId.getDesc(obfuscated), false));
					part++;
				}
			} else if (part == 1) { // remove everything up to ISTORE (exclusive), which stores the block id in a local var
				if (insn.getOpcode() == Opcodes.ISTORE) {
					part++;
					break; // nothing else to do, the msb id query returns always null
				} else {
					iterator.remove();
				}
			}
		}

		return part == 2;
	}
}
