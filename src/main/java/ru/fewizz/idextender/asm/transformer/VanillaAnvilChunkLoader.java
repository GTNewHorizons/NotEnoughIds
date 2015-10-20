package ru.fewizz.idextender.asm.transformer;

import java.util.ListIterator;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import ru.fewizz.idextender.asm.AsmUtil;
import ru.fewizz.idextender.asm.IClassNodeTransformer;
import ru.fewizz.idextender.asm.Name;

public class VanillaAnvilChunkLoader implements IClassNodeTransformer {
	@Override
	public boolean transform(ClassNode cn, boolean obfuscated) {
		MethodNode method = AsmUtil.findMethod(cn, Name.acl_writeChunkToNBT);
		if (method == null || !transformWriteChunkToNBT(cn, method, obfuscated)) return false;

		method = AsmUtil.findMethod(cn, Name.acl_readChunkFromNBT);
		if (method == null || !transformReadChunkFromNBT(cn, method, obfuscated)) return false;

		return true;
	}

	private boolean transformWriteChunkToNBT(ClassNode cn, MethodNode method, boolean obfuscated) {
		InsnList code = method.instructions;

		for (ListIterator<AbstractInsnNode> iterator = code.iterator(); iterator.hasNext();) {
			AbstractInsnNode insn = iterator.next();

			if (insn.getOpcode() == Opcodes.LDC && ((LdcInsnNode) insn).cst.equals("Blocks")) {
				iterator.remove();
				iterator.next(); 
				iterator.next(); iterator.remove(); // remove INVOKEVIRTUAL ExtendedBlockStorage.getBlockLSBArray
				iterator.next(); iterator.remove(); // remove INVOKEVIRTUAL NBTTagCompound.setByteArray
				
				iterator.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
						Name.hooks.get(obfuscated),
						Name.hooks_writeChunkToNbt.get(obfuscated),
						Name.hooks_writeChunkToNbt.getDesc(obfuscated), false));

				return true;
			}
		}

		return false;
	}

	private boolean transformReadChunkFromNBT(ClassNode cn, MethodNode method, boolean obfuscated) {
		InsnList code = method.instructions;
		int part = 0;

		for (ListIterator<AbstractInsnNode> iterator = code.iterator(); iterator.hasNext();) {
			AbstractInsnNode insn = iterator.next();

			if (part == 0) {
				if (insn.getOpcode() == Opcodes.LDC && ((LdcInsnNode) insn).cst.equals("Blocks")) {
					// ExtendedBlockStorage, NBTTagCompound are on the stack
					iterator.set(new MethodInsnNode(Opcodes.INVOKESTATIC,
							Name.hooks.get(obfuscated),
							Name.hooks_readChunkFromNbt.get(obfuscated),
							Name.hooks_readChunkFromNbt.getDesc(obfuscated), false));
					part++;
				}
			} else if (part == 1) {
				iterator.remove();

				if (insn.getOpcode() == Opcodes.INVOKEVIRTUAL) {
					MethodInsnNode node = (MethodInsnNode) insn;

					if (node.owner.equals(Name.extendedBlockStorage.get(obfuscated)) &&
							node.name.equals(Name.ebs_setBlockMSBArray.get(obfuscated)) &&
							node.desc.equals(Name.ebs_setBlockMSBArray.getDesc(obfuscated))) {
						part++;
					}
				}
			} else {
				if (insn.getType() == AbstractInsnNode.FRAME) {
					iterator.remove();
					break;
				}
			}
		}

		return part == 2;
	}
}
