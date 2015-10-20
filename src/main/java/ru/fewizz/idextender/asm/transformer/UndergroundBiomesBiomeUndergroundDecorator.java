package ru.fewizz.idextender.asm.transformer;

import java.util.ListIterator;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import ru.fewizz.idextender.asm.AsmTransformException;
import ru.fewizz.idextender.asm.AsmUtil;
import ru.fewizz.idextender.asm.IClassNodeTransformer;
import ru.fewizz.idextender.asm.Name;

public class UndergroundBiomesBiomeUndergroundDecorator implements IClassNodeTransformer {
	@Override
	public void transform(ClassNode cn, boolean obfuscated) {
		MethodNode method = AsmUtil.findMethod(cn, Name.ub_bud_replaceChunkOres_world);
		transformReplaceChunkOres(cn, method, obfuscated, 1);

		method = AsmUtil.findMethod(cn, Name.ub_bud_replaceChunkOres_iChunkProvider);
		transformReplaceChunkOres(cn, method, obfuscated, 0);
	}

	private void transformReplaceChunkOres(ClassNode cn, MethodNode method, boolean obfuscated, int varOffset) {
		InsnList code = method.instructions;
		int part = 0;

		for (ListIterator<AbstractInsnNode> iterator = code.iterator(); iterator.hasNext();) {
			AbstractInsnNode insn = iterator.next();

			if (part == 0) { // find ExtendedBlockStorage.getBlockLSBArray, replace with Hooks.getBlockId
				if (insn.getOpcode() == Opcodes.INVOKEVIRTUAL &&
						Name.ebs_getBlockLSBArray.matches((MethodInsnNode) insn, obfuscated)) {
					iterator.remove();
					// ExtendedBlockStorage is on the stack
					iterator.add(new VarInsnNode(Opcodes.ILOAD, 9 + varOffset)); // x
					iterator.add(new VarInsnNode(Opcodes.ILOAD, 18 + varOffset)); // y
					iterator.add(new VarInsnNode(Opcodes.ILOAD, 10 + varOffset)); // z
					iterator.add(Name.hooks_getBlockId.staticInvocation(obfuscated));
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

		if (part == 2) throw new AsmTransformException("no match for part "+part);
	}
}
