package ru.fewizz.idextender.asm.transformer;

import java.util.ListIterator;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;

import ru.fewizz.idextender.asm.AsmTransformException;
import ru.fewizz.idextender.asm.AsmUtil;
import ru.fewizz.idextender.asm.Constants;
import ru.fewizz.idextender.asm.IClassNodeTransformer;
import ru.fewizz.idextender.asm.Name;

public class VanillaS21PacketChunkData implements IClassNodeTransformer {
	@Override
	public void transform(ClassNode cn) {
		AsmUtil.transformIntConst(cn, "<clinit>", Constants.vanillaSize, Constants.newSize);
		AsmUtil.transformIntConst(cn, Name.s21_undefined1, Constants.vanillaSize, Constants.newSize);
		AsmUtil.transformIntConst(cn, Name.packet_readPacketData, Constants.vanillaEbsSize, Constants.newEbsSize);

		transformCreateData(cn);
	}

	private void transformCreateData(ClassNode cn) {
		InsnList code = AsmUtil.findMethod(cn, Name.s21_undefined2).instructions;

		for (ListIterator<AbstractInsnNode> iterator = code.iterator(); iterator.hasNext();) {
			AbstractInsnNode insn = iterator.next();

			if (insn.getOpcode() == INVOKEVIRTUAL) {
				MethodInsnNode node = (MethodInsnNode) insn;

				if (Name.ebs_getBlockLSBArray.matches(node)) {
					iterator.set(Name.hooks_getBlockData.staticInvocation());
					return;
				}
			}
		}

		throw new AsmTransformException("can't find getBlockLSBArray INVOKEVIRTUAL");
	}
}
