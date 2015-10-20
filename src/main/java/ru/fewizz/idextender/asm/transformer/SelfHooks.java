package ru.fewizz.idextender.asm.transformer;

import java.util.ListIterator;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import ru.fewizz.idextender.asm.AsmUtil;
import ru.fewizz.idextender.asm.IClassNodeTransformer;
import ru.fewizz.idextender.asm.Name;

public class SelfHooks implements IClassNodeTransformer {
	
	@Override
	public boolean transform(ClassNode cn, boolean obfuscated) {
		
		MethodNode method = AsmUtil.findMethod(cn, "get");
		if (method == null || !transformGet(cn, method)) return false;

		method = AsmUtil.findMethod(cn, "setBlockRefCount");
		if (method == null || !transformSetBlockRefCount(cn, method, obfuscated)) return false;
		
		method = AsmUtil.findMethod(cn, "setTickRefCount");
		if (method == null || !transformSetTickRefCount(cn, method, obfuscated)) return false;
		
		return true;
	}
	
	private boolean transformGet(ClassNode cn, MethodNode method){
		// replace with "return ebs.block16BArray;"
		InsnList code = method.instructions;

		code.clear();
		code.add(new VarInsnNode(Opcodes.ALOAD, 0));
		code.add(new FieldInsnNode(Opcodes.GETFIELD, Type.getArgumentTypes(method.desc)[0].getInternalName(), "block16BArray", "[S"));
		code.add(new InsnNode(Opcodes.ARETURN));

		method.localVariables = null;

		return true;
	}
	
	private boolean transformSetBlockRefCount(ClassNode cn, MethodNode method, boolean isObf){
		InsnList code = method.instructions;
		
		for(ListIterator<AbstractInsnNode> iterator = code.iterator(); iterator.hasNext(); ) {
			AbstractInsnNode insn = iterator.next();
			InsnList toInsert = new InsnList();
			
			toInsert.add(new VarInsnNode(Opcodes.ALOAD, 0));
			toInsert.add(new VarInsnNode(Opcodes.ILOAD, 1));
			toInsert.add(new FieldInsnNode(Opcodes.PUTFIELD, Name.extendedBlockStorage.get(isObf), !isObf ? "blockRefCount" : "field_76682_b", "I"));
			method.instructions.insert(insn, toInsert);
			
			method.maxStack = 3;
			
			break;
		}
		
		return true;
	}
	
	private boolean transformSetTickRefCount(ClassNode cn, MethodNode method, boolean isObf){
		InsnList code = method.instructions;
		
		for(ListIterator<AbstractInsnNode> iterator = code.iterator(); iterator.hasNext(); ) {
			AbstractInsnNode insn = iterator.next();
			InsnList toInsert = new InsnList();
			
			toInsert.add(new VarInsnNode(Opcodes.ALOAD, 0));
			toInsert.add(new VarInsnNode(Opcodes.ILOAD, 1));
			toInsert.add(new FieldInsnNode(Opcodes.PUTFIELD, Name.extendedBlockStorage.get(isObf), !isObf ? "tickRefCount" : "field_76683_c", "I"));
			method.instructions.insert(insn, toInsert);
			
			method.maxStack = 3;
			
			break;
		}
		
		return true;
	}
}
