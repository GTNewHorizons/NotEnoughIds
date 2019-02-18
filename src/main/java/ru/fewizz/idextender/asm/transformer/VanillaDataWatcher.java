package ru.fewizz.idextender.asm.transformer;

import java.util.ListIterator;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import ru.fewizz.idextender.IEConfig;
import ru.fewizz.idextender.asm.AsmUtil;
import ru.fewizz.idextender.asm.Constants;
import ru.fewizz.idextender.asm.IClassNodeTransformer;
import ru.fewizz.idextender.asm.Name;

public class VanillaDataWatcher implements IClassNodeTransformer {

	@Override
	public void transform(ClassNode cn) {
		if(!IEConfig.extendDataWatcher)
			return;
		AsmUtil.transformIntConst(cn, Name.dataWatcher_addObject, 31, Constants.maxDataWatcherId);

		////////////////////////////////////////////////////////////////////////////////////////////////

		MethodNode method = AsmUtil.findMethod(cn, Name.dataWatcher_writeWatchableObjectToPacketBuffer);
		AsmUtil.transformIntConst(cn, method, 31, Constants.maxDataWatcherId);
		AsmUtil.transformIntConst(cn, method, 255, 1023);
		AsmUtil.transformIntConst(cn, method, 5, 7);

		method.instructions.set(
			AsmUtil.findMethodInsnNode(method.instructions.getFirst(), "writeByte", null),
			new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/network/PacketBuffer", "writeShort", "(I)Lio/netty/buffer/ByteBuf;", false)
		);

		/////////////////////////////////////////////////////////////////////////////////////////////////

		method = AsmUtil.findMethod(cn, Name.dataWatcher_readWatchedListFromPacketBuffer);
		AsmUtil.transformIntConst(cn, method, 127, 32767);
		AsmUtil.transformIntConst(cn, method, 224, 896);
		AsmUtil.transformIntConst(cn, method, 31, Constants.maxDataWatcherId);
		AsmUtil.transformIntConst(cn, method, 5, 7);

		int num = 0;
		for (ListIterator<AbstractInsnNode> it = method.instructions.iterator(); it.hasNext();) {
			AbstractInsnNode insn = it.next();
			if (insn.getOpcode() == Opcodes.INVOKEVIRTUAL && ((MethodInsnNode) insn).name.equals("readByte")) {
				num++;
				if (num == 2)
					continue;
				it.set(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/network/PacketBuffer", "readShort", "()S", false));
				if (num == 3)
					break;
			}
		}

		//////////////////////////////////////////////////////////////////////////////////////////////////

		method = AsmUtil.findMethod(cn, Name.dataWatcher_writeWatchedListToPacketBuffer);
		AsmUtil.transformIntConst(cn, method, 127, 32767);

		method.instructions.set(
			AsmUtil.findMethodInsnNode(method.instructions.getFirst(), "writeByte", null),
			new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/network/PacketBuffer", "writeShort", "(I)Lio/netty/buffer/ByteBuf;", false)
		);

		//////////////////////////////////////////////////////////////////////////////////////////////////

		method = AsmUtil.findMethod(cn, Name.dataWatcher_func_151509_a);
		AsmUtil.transformIntConst(cn, method, 127, 32767);
		
		method.instructions.set(
			AsmUtil.findMethodInsnNode(method.instructions.getFirst(), "writeByte", null),
			new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/network/PacketBuffer", "writeShort", "(I)Lio/netty/buffer/ByteBuf;", false)
		);
	}
}