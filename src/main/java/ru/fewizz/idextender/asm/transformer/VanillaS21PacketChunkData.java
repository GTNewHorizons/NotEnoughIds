package ru.fewizz.idextender.asm.transformer;

import java.util.ListIterator;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import ru.fewizz.idextender.asm.AsmUtil;
import ru.fewizz.idextender.asm.Constants;
import ru.fewizz.idextender.asm.IClassNodeTransformer;
import ru.fewizz.idextender.asm.Name;

public class VanillaS21PacketChunkData implements IClassNodeTransformer {
	@Override
	public boolean transform(ClassNode cn, boolean obfuscated) {
		MethodNode method = AsmUtil.findMethod(cn, "<clinit>");
		if (method == null || !AsmUtil.transformInlinedSizeMethod(cn, method, Constants.vanillaSize, Constants.newSize)) return false;

		method = AsmUtil.findMethod(cn, "func_149275_c");
		if (method == null || !AsmUtil.transformInlinedSizeMethod(cn, method, Constants.vanillaSize, Constants.newSize)) return false;

		method = AsmUtil.findMethod(cn, Name.packet_readPacketData);
		if (method == null || !AsmUtil.transformInlinedSizeMethod(cn, method, Constants.vanillaEbsSize, Constants.newEbsSize)) return false;

		method = AsmUtil.findMethod(cn, "func_149269_a");
		if (method == null || !transformCreateData(cn, method, obfuscated)) return false;

		return true;
	}

	private boolean transformCreateData(ClassNode cn, MethodNode method, boolean obfuscated) {
		InsnList code = method.instructions;

		for (ListIterator<AbstractInsnNode> iterator = code.iterator(); iterator.hasNext();) {
			AbstractInsnNode insn = iterator.next();

			if (insn.getOpcode() == Opcodes.INVOKEVIRTUAL) {
				MethodInsnNode node = (MethodInsnNode) insn;

				if (node.owner.equals(Name.extendedBlockStorage.get(obfuscated)) &&
						node.name.equals(Name.ebs_getBlockLSBArray.get(obfuscated)) &&
						node.desc.equals(Name.ebs_getBlockLSBArray.getDesc(obfuscated))) {
					iterator.set(new MethodInsnNode(Opcodes.INVOKESTATIC,
							Name.hooks.get(obfuscated),
							Name.hooks_getBlockData.get(obfuscated),
							Name.hooks_getBlockData.getDesc(obfuscated), false));

					return true;
				}
			}
		}

		return false;
	}
}
