package ru.fewizz.idextender.asm.transformer;

import java.util.ListIterator;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import ru.fewizz.idextender.asm.AsmTransformException;
import ru.fewizz.idextender.asm.AsmUtil;
import ru.fewizz.idextender.asm.Constants;
import ru.fewizz.idextender.asm.IClassNodeTransformer;
import ru.fewizz.idextender.asm.Name;

public class VanillaS21PacketChunkData implements IClassNodeTransformer {
	@Override
	public void transform(ClassNode cn, boolean obfuscated) {
		MethodNode method = AsmUtil.findMethod(cn, "<clinit>");
		AsmUtil.transformInlinedSizeMethod(cn, method, Constants.vanillaSize, Constants.newSize, false);

		method = AsmUtil.findMethod(cn, "func_149275_c");
		AsmUtil.transformInlinedSizeMethod(cn, method, Constants.vanillaSize, Constants.newSize, false);

		method = AsmUtil.findMethod(cn, Name.packet_readPacketData);
		AsmUtil.transformInlinedSizeMethod(cn, method, Constants.vanillaEbsSize, Constants.newEbsSize, false);

		method = AsmUtil.findMethod(cn, "func_149269_a");
		transformCreateData(cn, method, obfuscated);
	}

	private void transformCreateData(ClassNode cn, MethodNode method, boolean obfuscated) {
		InsnList code = method.instructions;

		for (ListIterator<AbstractInsnNode> iterator = code.iterator(); iterator.hasNext();) {
			AbstractInsnNode insn = iterator.next();

			if (insn.getOpcode() == Opcodes.INVOKEVIRTUAL) {
				MethodInsnNode node = (MethodInsnNode) insn;

				if (Name.ebs_getBlockLSBArray.matches(node, obfuscated)) {
					iterator.set(Name.hooks_getBlockData.staticInvocation(obfuscated));
					return;
				}
			}
		}

		throw new AsmTransformException("can't find getBlockLSBArray INVOKEVIRTUAL");
	}
}
