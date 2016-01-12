package ru.fewizz.idextender.asm.transformer;

import java.util.ListIterator;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.MethodNode;

import ru.fewizz.idextender.IEConfig;
import ru.fewizz.idextender.asm.AsmUtil;
import ru.fewizz.idextender.asm.IClassNodeTransformer;
import ru.fewizz.idextender.asm.Name;
import scala.tools.nsc.backend.jvm.AsmUtils;

public class VanillaDataWatcher implements IClassNodeTransformer{

	@Override
	public void transform(ClassNode cn, boolean obfuscated) {
		if(IEConfig.extendDataWatcher){
			MethodNode method = AsmUtil.findMethod(cn, Name.dataWatcher_addObject);
			AsmUtil.transformInlinedSizeMethod(cn, method, 31, 63, false);
			
			////////////////////////////////////////////////////////////////////////////////////////////////
			
			method = AsmUtil.findMethod(cn, Name.dataWatcher_writeWatchableObjectToPacketBuffer);
			AsmUtil.transformInlinedSizeMethod(cn, method, 31, 63, false);
			AsmUtil.transformInlinedSizeMethod(cn, method, 255, 511, false);
			
			for (ListIterator<AbstractInsnNode> it = method.instructions.iterator(); it.hasNext();) {
				AbstractInsnNode insn = it.next();
				if(insn.getOpcode() == Opcodes.ICONST_5){
					it.set(new IntInsnNode(Opcodes.BIPUSH, 6));
					break;
				}
			}
			
			/////////////////////////////////////////////////////////////////////////////////////////////////
			
			method = AsmUtil.findMethod(cn, Name.dataWatcher_readWatchedListFromPacketBuffer);
			AsmUtil.transformInlinedSizeMethod(cn, method, 224, 448, false);
			AsmUtil.transformInlinedSizeMethod(cn, method, 31, 63, false);
			
			for (ListIterator<AbstractInsnNode> it = method.instructions.iterator(); it.hasNext();) {
				AbstractInsnNode insn = it.next();
				if(insn.getOpcode() == Opcodes.ICONST_5){
					it.set(new IntInsnNode(Opcodes.BIPUSH, 6));
					break;
				}
			}
		}
	}
}
