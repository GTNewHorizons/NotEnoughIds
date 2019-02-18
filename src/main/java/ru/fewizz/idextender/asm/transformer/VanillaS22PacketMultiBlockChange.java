package ru.fewizz.idextender.asm.transformer;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import ru.fewizz.idextender.asm.AsmUtil;
import ru.fewizz.idextender.asm.IClassNodeTransformer;
import ru.fewizz.idextender.asm.Name;

public class VanillaS22PacketMultiBlockChange implements IClassNodeTransformer {
	@Override
	public void transform(ClassNode cn) {
		MethodNode method = AsmUtil.findMethod(cn, Name.s22_init_server);
		method.maxStack+=2;
		InsnList code = method.instructions;
		code.set(AsmUtil.findInsnNode(code.getFirst(), ICONST_4), new InsnNode(ICONST_5));
		AbstractInsnNode getID =
		AsmUtil.findMethodInsnNode(code.getFirst(), Name.block_getIdFromBlock);
		code.insert(getID, new InsnNode(DUP2));
		
		InsnList il = new InsnList();
		il.add(new IntInsnNode(BIPUSH, 12));
		il.add(new InsnNode(ISHR));
		il.add(new MethodInsnNode(INVOKEVIRTUAL, "java/io/DataOutputStream", "write", "(I)V", false));
		code.insert(AsmUtil.findMethodInsnNode(getID, "writeShort", null), il);
	}
}
