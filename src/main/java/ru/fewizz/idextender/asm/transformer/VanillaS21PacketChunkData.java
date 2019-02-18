package ru.fewizz.idextender.asm.transformer;

import java.util.ListIterator;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import ru.fewizz.idextender.asm.AsmTransformException;
import ru.fewizz.idextender.asm.AsmUtil;
import ru.fewizz.idextender.asm.Constants;
import ru.fewizz.idextender.asm.IClassNodeTransformer;
import ru.fewizz.idextender.asm.Name;

public class VanillaS21PacketChunkData implements IClassNodeTransformer {
	@Override
	public void transform(ClassNode cn) {
		//cn.fields.add(new FieldNode(ACC_PUBLIC, "readSize", "I", null, -1));
		AsmUtil.transformIntConst(cn, "<clinit>", Constants.vanillaSize, Constants.newSize);
		AsmUtil.transformIntConst(cn, Name.s21_undefined1, Constants.vanillaSize, Constants.newSize);
		
		MethodNode read = AsmUtil.findMethod(cn, Name.packet_readPacketData);
		AsmUtil.transformIntConst(cn, read, Constants.vanillaEbsSize, Constants.newEbsSize);
		AbstractInsnNode inflate = AsmUtil.findMethodInsnNode(read.instructions.getFirst(), "inflate", null);
		read.instructions.remove(inflate.getNext());
		//read.instructions.set(inflate.getNext(), Name.s21_readSize.putField()); // saving instead popping
		
		InsnList il = new InsnList(); // then reallocating array
		il.add(new VarInsnNode(ALOAD, 0));
		//il.add(new InsnNode(DUP));
		il.add(Name.s21_data.getField());
		il.add(new InsnNode(SWAP));
		il.add(new MethodInsnNode(INVOKESTATIC, "java/util/Arrays", "copyOf", "([BI)[B", false));
		il.add(new VarInsnNode(ALOAD, 0));
		il.add(new InsnNode(SWAP));
		il.add(Name.s21_data.putField());
		read.instructions.insert(inflate, il);
		
		transformCreateData(cn);
	}

	private void transformCreateData(ClassNode cn) {
		InsnList code = AsmUtil.findMethod(cn, Name.s21_undefined2).instructions;

		for (ListIterator<AbstractInsnNode> iterator = code.iterator(); iterator.hasNext();) {
			AbstractInsnNode insn = iterator.next();

			if (insn.getOpcode() == INVOKEVIRTUAL && Name.ebs_getBlockLSBArray.matches((MethodInsnNode)insn)) {
				iterator.set(Name.hooks_getBlockData.invokeStatic());
				return;
			}
		}

		throw new AsmTransformException("can't find getBlockLSBArray INVOKEVIRTUAL");
	}
}
