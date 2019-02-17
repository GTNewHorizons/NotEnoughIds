package ru.fewizz.idextender.asm.transformer;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import ru.fewizz.idextender.asm.AsmUtil;
import ru.fewizz.idextender.asm.IClassNodeTransformer;
import ru.fewizz.idextender.asm.Name;

public class VanillaS26PacketMapChunkBulk implements IClassNodeTransformer {
	@Override
	public void transform(ClassNode cn) {
		MethodNode method = AsmUtil.findMethod(cn, Name.packet_readPacketData);
		AsmUtil.transformIntConst(cn, method, 8192, 12288);
	}
}
