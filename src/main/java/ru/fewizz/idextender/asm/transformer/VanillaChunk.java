package ru.fewizz.idextender.asm.transformer;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import ru.fewizz.idextender.asm.AsmUtil;
import ru.fewizz.idextender.asm.IClassNodeTransformer;
import ru.fewizz.idextender.asm.Name;

public class VanillaChunk implements IClassNodeTransformer {
	@Override
	public void transform(ClassNode cn) {
		MethodNode method = AsmUtil.findMethod(cn, Name.chunk_fillChunk, true);

		if (method == null)
			return; // the method doesn't exist on the server side

		method.localVariables = null;
		
		AbstractInsnNode loopBegin = AsmUtil.findVarInsnNode(method.instructions.getFirst(), ISTORE, 8).getPrevious(); // Iconst0
		AbstractInsnNode nextLoopBegin = AsmUtil.findVarInsnNode(loopBegin, ISTORE, 8).getPrevious(); // Too
		
		AsmUtil.removeRange(method.instructions, loopBegin, nextLoopBegin);
		InsnList hook = new InsnList();
		hook.add(new VarInsnNode(ALOAD, 0)); // chunk
		hook.add(new VarInsnNode(ALOAD, 1)); // data
		hook.add(new VarInsnNode(ILOAD, 2)); // bits
		hook.add(new VarInsnNode(ILOAD, 4)); // flag
		hook.add(Name.hooks_copyBlockDataFromPacket.invokeStatic());
		hook.add(new VarInsnNode(ISTORE, 6)); // storing offset
		method.instructions.insertBefore(nextLoopBegin, hook);
	}
}
