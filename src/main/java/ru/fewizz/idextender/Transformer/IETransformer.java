package ru.fewizz.idextender.Transformer;

import java.util.ListIterator;

import net.minecraft.launchwrapper.IClassTransformer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class IETransformer implements IClassTransformer{
	boolean isObf = true;
	
	ClassNode cn;
    ClassReader reader;
    ClassWriter writer;
    public static Logger logger = LogManager.getLogger("NEID");
    
	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		
        if("net.minecraft.block.BlockFire".equals(transformedName)){
        	///////////////////////////////
        	start(basicClass, "BlockFire");
            boolean finded = false;
        	
            for(MethodNode method : cn.methods) {
            	if(!finded){
            		if(method.name.equals("canBlockCatchFire")){
            			isObf = false;
            			finded = true;
            		}
            		else
            			isObf = true;
            	}
            	
            	InsnList code = method.instructions;
        		
        		for(ListIterator<AbstractInsnNode> iterator = code.iterator(); iterator.hasNext(); ) {
                    AbstractInsnNode insn = iterator.next();
                    
                    if(insn.getOpcode() == Opcodes.SIPUSH && insn.getType() == insn.INT_INSN && ((IntInsnNode)insn).operand == 4096){
                    	InsnList toInsert = new InsnList();
                    	toInsert.set(insn, new FieldInsnNode(Opcodes.GETSTATIC, "ru/fewizz/idextender/Hooks", "NUM_BLOCK_IDS", "I"));
                    	method.instructions.insert(toInsert);
                    }   
        		}
            } 
            
            return end(basicClass, "BlockFire");
            ////////////////////////////////////
        } 
        
        if("net.minecraft.stats.StatList".equals(transformedName)){
        	//////////////////////////////
        	start(basicClass, "StatList");
        	
            for(MethodNode method : cn.methods) {
            	InsnList code = method.instructions;
        		
        		for(ListIterator<AbstractInsnNode> iterator = code.iterator(); iterator.hasNext(); ) {
                    AbstractInsnNode insn = iterator.next();
                    
                    if(insn.getOpcode() == Opcodes.SIPUSH && insn.getType() == insn.INT_INSN){
                    	if(((IntInsnNode)insn).operand == 4096){
                        	InsnList toInsert = new InsnList();
                        	toInsert.set(insn, new FieldInsnNode(Opcodes.GETSTATIC, "ru/fewizz/idextender/Hooks", "NUM_BLOCK_IDS", "I"));
                        	method.instructions.insert(toInsert);
                    	}
                    	if(((IntInsnNode)insn).operand == 32000){
                        	InsnList toInsert = new InsnList();
                        	toInsert.set(insn, new FieldInsnNode(Opcodes.GETSTATIC, "ru/fewizz/idextender/Hooks", "NUM_ITEM_IDS", "I"));
                        	method.instructions.insert(toInsert);
                    	}
                    }   
        		}
            } 
            
            return end(basicClass, "StatList");
            ///////////////////////////////////
        }
        
        if("cpw.mods.fml.common.registry.GameData".equals(transformedName) || "cpw.mods.fml.common.registry.FMLControlledNamespacedRegistry".equals(transformedName) || "cpw.mods.fml.common.registry.GameRegistry".equals(transformedName)){
        	///////////////////////////////
        	start(basicClass, "GameData");

        	for(MethodNode method : cn.methods) {
            	InsnList code = method.instructions;
        		
        		for(ListIterator<AbstractInsnNode> iterator = code.iterator(); iterator.hasNext(); ) {
                    AbstractInsnNode insn = iterator.next();
                    
                    if(insn.getOpcode() == Opcodes.SIPUSH && insn.getType() == insn.INT_INSN){
                    	if(((IntInsnNode)insn).operand == 4095){
                        	InsnList toInsert = new InsnList();
                        	toInsert.set(insn, new IntInsnNode(Opcodes.SIPUSH, 31999));
                        	method.instructions.insert(toInsert);
                    	}
                    }   
        		}
            } 
        	
            return end(basicClass, "GameData");
            ///////////////////////////////////
        }
        
        if("net.minecraft.world.chunk.storage.AnvilChunkLoader".equals(transformedName)){
        	//////////////////////////////////////
        	start(basicClass, "AnvilChunkLoader");
        	for(MethodNode method : cn.methods) {
        		if("writeChunkToNBT".equals(method.name) || ("a".equals(method.name) && "(Lapx;Lahb;Ldh;)V".equals(method.desc))){
        			InsnList code = method.instructions;
        			
        			for(ListIterator<AbstractInsnNode> iterator = code.iterator(); iterator.hasNext(); ) {
        				AbstractInsnNode insn = iterator.next();
                    
        				if(insn.getType() == insn.LDC_INSN && ((LdcInsnNode)insn).cst.equals("Y")){
        					insn = insn.getNext().getNext().getNext().getNext().getNext().getNext().getNext().getNext().getNext().getNext().getNext();
        					while(true)
        	                {
        						if(insn.getNext().getOpcode() == Opcodes.INVOKEVIRTUAL && ((MethodInsnNode)insn.getNext()).name.equals(!isObf ? "setByteArray" : "a") && ((MethodInsnNode)insn.getNext()).desc.equals("(Ljava/lang/String;[B)V")){// && (insn.getOpcode() == Opcodes.GETFIELD && insn.getOpcode() == Opcodes.INVOKEVIRTUAL)){
        							insn = insn.getNext();
            	                    method.instructions.remove(insn.getPrevious());
            	                    insn = insn.getNext();
            	                    method.instructions.remove(insn.getPrevious());
            	                    break; 
        						}
        						insn = insn.getNext();
        	                    method.instructions.remove(insn.getPrevious());
        	                }
        					
        					
        					InsnList toInsert = new InsnList();
        					toInsert.add(new VarInsnNode(Opcodes.ALOAD, 11));
        					toInsert.add(new VarInsnNode(Opcodes.ALOAD, 9));
        					toInsert.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "ru/fewizz/idextender/Hooks", "writeChunkToNbt", !isObf ? "(Lnet/minecraft/world/chunk/storage/ExtendedBlockStorage;Lnet/minecraft/nbt/NBTTagCompound;)V" : "(Lapz;Ldh;)V", false));
        					method.instructions.insert(insn.getNext(), toInsert);
        					break;
        				}   
        			}
        		}
        		
        		if("readChunkFromNBT".equals(method.name) || ("a".equals(method.name) && "(Lahb;Ldh;)Lapx;".equals(method.desc))){
        			InsnList code = method.instructions;
            		
        			for(ListIterator<AbstractInsnNode> iterator = code.iterator(); iterator.hasNext(); ) {
        				AbstractInsnNode insn = iterator.next();
        				
        				if(insn.getOpcode() == Opcodes.ILOAD && ((VarInsnNode)insn).var == 9 && insn.getNext().getOpcode() == Opcodes.INVOKESPECIAL){
        					
        					insn = insn.getNext().getNext().getNext();
        					while(true)
        	                {
        						if(insn.getNext().getOpcode() == Opcodes.INVOKEVIRTUAL && ((MethodInsnNode)insn.getNext()).name.equals(!isObf ? "setBlockMSBArray" : "a") && ((MethodInsnNode)insn.getNext()).desc.equals(!isObf ? "(Lnet/minecraft/world/chunk/NibbleArray;)V" : "(Lapv;)V")){//(Lnet/minecraft/world/chunk/NibbleArray;)V
        							insn = insn.getNext();
            	                    method.instructions.remove(insn.getPrevious());
            	                    insn = insn.getNext();
            	                    method.instructions.remove(insn.getPrevious());
            	                    break; 
        						}
        						insn = insn.getNext();
        	                    method.instructions.remove(insn.getPrevious());
        	                }
        					
        					InsnList toInsert = new InsnList();
        					toInsert.add(new VarInsnNode(Opcodes.ALOAD, 13));
        					toInsert.add(new VarInsnNode(Opcodes.ALOAD, 11));
        					toInsert.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "ru/fewizz/idextender/Hooks", "readChunkFromNbt", !isObf ? "(Lnet/minecraft/world/chunk/storage/ExtendedBlockStorage;Lnet/minecraft/nbt/NBTTagCompound;)V" : "(Lapz;Ldh;)V", false));
        					method.instructions.insert(insn.getNext(), toInsert);
        					break;
        				}
        			}
        			break;
        		}
            } 
        	
        	return end(basicClass, "AnvilChunkLoader");
            ///////////////////////////////////////////
        }
        
        if("net.minecraft.world.chunk.storage.ExtendedBlockStorage".equals(transformedName)){
        	//////////////////////////////////////////
        	start(basicClass, "ExtendedBlockStorage");
        	
        	cn.fields.add(new FieldNode(Opcodes.ACC_PUBLIC, "block16BArray", "[S", null, null));
        	
        	for(MethodNode method : cn.methods) {
        		if("<init>".equals(method.name) || "ExtendedBlockStorage".equals(method.name)){
        			InsnList code = method.instructions;
            		
        			for(ListIterator<AbstractInsnNode> iterator = code.iterator(); iterator.hasNext(); ) {
        				AbstractInsnNode insn = iterator.next();
        				
        				insn = insn.getNext().getNext();
        			
        				InsnList toInsert = new InsnList();
        				
        				toInsert.add(new VarInsnNode(Opcodes.ALOAD, 0));
        				toInsert.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "ru/fewizz/idextender/Hooks", "create16BArray", "()[S", false));
        				toInsert.add(new FieldInsnNode(Opcodes.PUTFIELD, !isObf ? "net/minecraft/world/chunk/storage/ExtendedBlockStorage" : "apz", "block16BArray", "[S"));        				
    					method.instructions.insert(insn, toInsert);
    					break;
        			}
        		
        		}
        		
        		if("getBlockByExtId".equals(method.name) || ("a".equals(method.name) && "(III)Laji;".equals(method.desc))){
        			InsnList code = method.instructions;
        		
        			for(ListIterator<AbstractInsnNode> iterator = code.iterator(); iterator.hasNext(); ) {
        				AbstractInsnNode insn = iterator.next();
        				
        				insn = insn.getNext().getNext();
        			
        				InsnList toInsert = new InsnList();
        				toInsert.add(new VarInsnNode(Opcodes.ILOAD, 1));
        				toInsert.add(new VarInsnNode(Opcodes.ILOAD, 2));
        				toInsert.add(new VarInsnNode(Opcodes.ILOAD, 3));
        				toInsert.add(new VarInsnNode(Opcodes.ALOAD, 0));
        				toInsert.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "ru/fewizz/idextender/Hooks", "getBlockById", !isObf ? "(IIILnet/minecraft/world/chunk/storage/ExtendedBlockStorage;)Lnet/minecraft/block/Block;" : "(IIILapz;)Laji;", false));
        				toInsert.add(new InsnNode(Opcodes.ARETURN));
    					method.instructions.insert(insn, toInsert);
    					break;
        			}
        		}
        		
        		if("func_150818_a".equals(method.name) || ("a".equals(method.name) && "(IIILaji;)V".equals(method.desc))){
        			InsnList code = method.instructions;
            		
        			for(ListIterator<AbstractInsnNode> iterator = code.iterator(); iterator.hasNext(); ) {
        				AbstractInsnNode insn = iterator.next();
        				
        				if(insn.getOpcode() == Opcodes.F_APPEND && insn.getNext().getOpcode() == Opcodes.ILOAD && ((VarInsnNode)insn.getNext()).var == 5){
        					InsnList toInsert = new InsnList();

        					toInsert.set(insn, new VarInsnNode(Opcodes.ALOAD, 0));
        					insn = insn.getNext();
        					toInsert.set(insn, new VarInsnNode(Opcodes.ILOAD, 1));
        					method.instructions.insert(toInsert);
        					
        					toInsert.add(new VarInsnNode(Opcodes.ILOAD, 2));
        					toInsert.add(new VarInsnNode(Opcodes.ILOAD, 3));
        					toInsert.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, !isObf ? "net/minecraft/world/chunk/storage/ExtendedBlockStorage" : "apz", !isObf ? "getBlockByExtId" : "a", !isObf ? "(III)Lnet/minecraft/block/Block;" : "(III)Laji;", false));
        					method.instructions.insert(insn, toInsert);
        				}
        				
        				if(insn.getOpcode() == Opcodes.INVOKESTATIC && ((MethodInsnNode)insn).name.equals(!isObf ? "getIdFromBlock" : "b") && insn.getNext().getOpcode() == Opcodes.ISTORE && ((VarInsnNode)insn.getNext()).var == 7 && ((MethodInsnNode)insn).desc.equals(!isObf ? "(Lnet/minecraft/block/Block;)I" : "(Laji;)I")){
        					InsnList toInsert = new InsnList();
        					
        					toInsert.add(new VarInsnNode(Opcodes.ILOAD, 7));
        					toInsert.add(new VarInsnNode(Opcodes.ILOAD, 1));
        					toInsert.add(new VarInsnNode(Opcodes.ILOAD, 2));
        					toInsert.add(new VarInsnNode(Opcodes.ILOAD, 3));
        					toInsert.add(new VarInsnNode(Opcodes.ALOAD, 0));
        					toInsert.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "ru/fewizz/idextender/Hooks", "hook1", !isObf ? "(IIIILnet/minecraft/world/chunk/storage/ExtendedBlockStorage;)V" : "(IIIILapz;)V", false));
        					method.instructions.insert(insn.getNext(), toInsert);
        					break;
        				}
        			}
        		}
        		
        		if("getBlockMSBArray".equals(method.name) || ("i".equals(method.name) && "()Lapv;".equals(method.desc))){
        			InsnList code = method.instructions;
            		
        			for(ListIterator<AbstractInsnNode> iterator = code.iterator(); iterator.hasNext(); ){
        				AbstractInsnNode insn = iterator.next();
        				InsnList toInsert = new InsnList();
        				insn = insn.getNext().getNext().getNext();
        				toInsert.add(new InsnNode(Opcodes.ACONST_NULL));
        				toInsert.add(new InsnNode(Opcodes.ARETURN));
        				method.instructions.insert(toInsert);
        				break;
        			}
        		}
        		
        		if("isEmpty".equals(method.name) || ("a".equals(method.name) && "()Z".equals(method.desc))){
        			System.out.println("Nasel!");
        			InsnList code = method.instructions;
            		
        			for(ListIterator<AbstractInsnNode> iterator = code.iterator(); iterator.hasNext(); ){
        				AbstractInsnNode insn = iterator.next();
        				InsnList toInsert = new InsnList();
        				toInsert.add(new InsnNode(Opcodes.ICONST_0));
        				toInsert.add(new InsnNode(Opcodes.IRETURN));
        				method.instructions.insert(insn.getNext().getNext(), toInsert);
        				break;
        			}
        		}
        	}
        	
        	return end(basicClass, "ExtendedBlockStorage");
            ///////////////////////////////////////////////
        }
        
        if("net.minecraft.network.play.server.S21PacketChunkData".equals(transformedName)){
        	////////////////////////////////////////
        	start(basicClass, "S21PacketChunkData");

        	for(MethodNode method : cn.methods) {
        		InsnList code2 = method.instructions;
        		for(ListIterator<AbstractInsnNode> iterator = code2.iterator(); iterator.hasNext(); ){
        			AbstractInsnNode insn = iterator.next();
        			
        			if(insn.getType() == insn.LDC_INSN && ((LdcInsnNode)insn).cst.equals(new Integer(196864))){
        				InsnList toInsert = new InsnList();
        				
        				toInsert.set(insn, new LdcInsnNode(new Integer(new Integer(262144))));
        				method.instructions.insert(insn, toInsert);
        			}
        		}
        		
        		if("func_149269_a".equals(method.name) || ("a".equals(method.name) && "(Lapx;ZI)Lgy;".equals(method.desc))){
        			InsnList code = method.instructions;

        			for(ListIterator<AbstractInsnNode> iterator = code.iterator(); iterator.hasNext(); ) {
        				AbstractInsnNode insn = iterator.next();
        				
        				if(insn.getOpcode() == Opcodes.INVOKEVIRTUAL && ((MethodInsnNode)insn).name.equals(!isObf ? "getBlockLSBArray" : "g")){
        					InsnList toInsert = new InsnList();
        					
        					toInsert.set(insn, new MethodInsnNode(Opcodes.INVOKESTATIC, "ru/fewizz/idextender/Hooks", "get16BitBlockArray", !isObf ? "(Lnet/minecraft/world/chunk/storage/ExtendedBlockStorage;)[B" : "(Lapz;)[B", false));
        					toInsert.insert(toInsert);
        				}
        				
        				if(insn.getOpcode() == Opcodes.ILOAD && insn.getNext().getOpcode() == Opcodes.IFLE){
        					
        					InsnList toInsert = new InsnList();
        					toInsert.set(insn, new MethodInsnNode(Opcodes.INVOKESTATIC, "ru/fewizz/idextender/Hooks", "zero", "()I", false));
        					method.instructions.insert(toInsert);
        						
        				}
        			}
        		}
        	} 

        	return end(basicClass, "S21PacketChunkData");
        	/////////////////////////////////////////////
        }
        
        if("ru.fewizz.idextender.Hooks".equals(transformedName)){
        	////////////////////////////////
        	start(basicClass, "MyHooks =)");
        	
        	for(MethodNode method : cn.methods) {
        		if("get".equals(method.name)){
        			InsnList code = method.instructions;

        			for(ListIterator<AbstractInsnNode> iterator = code.iterator(); iterator.hasNext(); ) {
        				AbstractInsnNode insn = iterator.next();
        				
        				if(insn.getOpcode() == Opcodes.ACONST_NULL){
        					InsnList toInsert = new InsnList();
        					
    	                    insn = insn.getNext();
    	                    method.instructions.remove(insn.getPrevious());
    	                    insn = insn.getNext();
    	                    method.instructions.remove(insn.getPrevious());
    	                    
    	                    toInsert.add(new VarInsnNode(Opcodes.ALOAD, 0));
    	                    toInsert.add(new FieldInsnNode(Opcodes.GETFIELD, !isObf ? "net/minecraft/world/chunk/storage/ExtendedBlockStorage" : "apz", "block16BArray", "[S"));
    	                    toInsert.add(new InsnNode(Opcodes.ARETURN));
    	                    method.instructions.insert(insn, toInsert);
    	                    
        					break;
        				}
        			}
        		}
        	}
        	
        	return end(basicClass, "MyHooks =)");
        	/////////////////////////////////////
        }
        
        if("net.minecraft.world.chunk.Chunk".equals(transformedName)){
        	start(basicClass, "Chunk");
        	///////////////////////////
        	
        	for(MethodNode method : cn.methods) {
        		if("fillChunk".equals(method.name) || ("a".equals(method.name) && "([BIIZ)V".equals(method.desc))){
        			InsnList code = method.instructions;

        			int i = 0;
        		
        			for(ListIterator<AbstractInsnNode> iterator = code.iterator(); iterator.hasNext(); ) {
        				AbstractInsnNode insn = iterator.next();
        				
        				
        				if(insn.getOpcode() == Opcodes.ALOAD && insn.getNext().getOpcode() == Opcodes.GETFIELD && insn.getNext().getNext().getOpcode() == Opcodes.ILOAD && insn.getNext().getNext().getNext().getOpcode() == Opcodes.AALOAD){
        					if(i != 1){
        						i++;
        					}
        					else{
        						InsnList toInsert = new InsnList();
        					
    	                    	insn = insn.getNext();
    	                    	method.instructions.remove(insn.getPrevious());
    	                    	insn = insn.getNext();
    	                    	method.instructions.remove(insn.getPrevious());
    	                    	insn = insn.getNext();
    	                    	method.instructions.remove(insn.getPrevious());
        						insn = insn.getNext();
    	                    	method.instructions.remove(insn.getPrevious());
    	                    	insn = insn.getNext();
    	                    	method.instructions.remove(insn.getPrevious());
    	                    
    	                    	toInsert.add(new IntInsnNode(Opcodes.SIPUSH, 8192));
    	                    	toInsert.add(new IntInsnNode(Opcodes.NEWARRAY, Opcodes.T_BYTE));
    	                    	method.instructions.insert(insn.getPrevious(), toInsert);
    	                    	
    	                    	toInsert.add(new VarInsnNode(Opcodes.ALOAD, 9));
            					toInsert.add(new VarInsnNode(Opcodes.ALOAD, 0));
            					toInsert.add(new FieldInsnNode(Opcodes.GETFIELD, !isObf ? "net/minecraft/world/chunk/Chunk" : "apx", !isObf ? "storageArrays" : "u", !isObf ? "[Lnet/minecraft/world/chunk/storage/ExtendedBlockStorage;" : "[Lapz;"));
            					toInsert.add(new VarInsnNode(Opcodes.ILOAD, 8));
            					toInsert.add(new InsnNode(Opcodes.AALOAD));
            					toInsert.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "ru/fewizz/idextender/Hooks", "set16BitBlockArray", !isObf ? "([BLnet/minecraft/world/chunk/storage/ExtendedBlockStorage;)V" : "([BLapz;)V", false));
            					method.instructions.insert(insn.getNext().getNext().getNext().getNext().getNext().getNext().getNext().getNext().getNext().getNext().getNext().getNext().getNext().getNext().getNext().getNext(), toInsert);
            					break;
        					}
        				}
    				}
        		}
        	}
        	
        	for(MethodNode method : cn.methods) {
        		if("fillChunk".equals(method.name) || ("a".equals(method.name) && "([BIIZ)V".equals(method.desc))){
        			InsnList code = method.instructions;

        			int i1 = 0;
        		
        			for(ListIterator<AbstractInsnNode> iterator = code.iterator(); iterator.hasNext(); ) {
        				AbstractInsnNode insn = iterator.next();
        				
        				if(insn.getOpcode() == Opcodes.ICONST_0){
        					if(i1 != 10){
        						i1++;
        					}
        					else{
        						InsnList toInsert = new InsnList();
        						
        						toInsert.set(insn, new LdcInsnNode(new Integer(1000000)));
        						method.instructions.insert(toInsert);
        						break;
        					}
        				}
    				}
        		}
        	}
        	
        	return end(basicClass, "Chunk");
        	////////////////////////////////
        }
        
        if("net.minecraft.client.network.NetHandlerPlayClient".equals(transformedName)){
        	start(basicClass, "NetHandlerPlayClient");
        	//////////////////////////////////////////
        	
        	for(MethodNode method : cn.methods) {
        		if("handleMultiBlockChange".equals(method.name) || ("a".equals(method.name) && "(Lgk;)V".equals(method.desc))){
        			InsnList code = method.instructions;
        			for(ListIterator<AbstractInsnNode> iterator = code.iterator(); iterator.hasNext(); ) {
        				AbstractInsnNode insn = iterator.next();
    				
        				if(insn.getOpcode() == Opcodes.SIPUSH && ((IntInsnNode)insn).operand == 4095){
        					InsnList toInsert = new InsnList();
    					
        					method.instructions.remove(insn.getPrevious().getPrevious().getPrevious().getPrevious().getPrevious().getPrevious().getPrevious().getPrevious());
        					method.instructions.remove(insn.getPrevious().getPrevious().getPrevious().getPrevious().getPrevious().getPrevious().getPrevious());
        					method.instructions.remove(insn.getPrevious().getPrevious().getPrevious().getPrevious().getPrevious().getPrevious());
        					
        					insn = insn.getPrevious().getPrevious();
        					method.instructions.remove(insn.getPrevious());
        					insn = insn.getNext();
	                    	method.instructions.remove(insn.getPrevious());
	                    	insn = insn.getNext();
	                    	method.instructions.remove(insn.getPrevious());
	                    	insn = insn.getNext();
	                    	method.instructions.remove(insn.getPrevious());
	                    	
	                    	toInsert.add(new VarInsnNode(Opcodes.ALOAD, 4));
	                    	toInsert.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/io/DataInputStream", "readShort", "()S", false));
	                    	toInsert.add(new LdcInsnNode(new Integer(65535)));
	                    	method.instructions.insert(insn.getPrevious(), toInsert);
	                    	
	                    	toInsert.set(insn.getNext(), new VarInsnNode(Opcodes.ISTORE, 8));
	                    	method.instructions.insert(toInsert);
	                    	
	                    	insn = insn.getNext().getNext().getNext().getNext();
	                    	
	                    	insn = insn.getNext();
	                    	method.instructions.remove(insn.getPrevious());
	                    	insn = insn.getNext();
	                    	method.instructions.remove(insn.getPrevious());
	                    	
	                    	toInsert.add(new VarInsnNode(Opcodes.ALOAD, 4));
	                    	toInsert.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/io/DataInputStream", "readByte", "()B", false));
	                    	toInsert.add(new IntInsnNode(Opcodes.SIPUSH, 255));
	                    	method.instructions.insert(insn.getPrevious(), toInsert);
        				}
        			}
        		}
        	}
        		
        	return end(basicClass, "NetHandlerPlayClient");
        	///////////////////////////////////////////////
        }
        
        if("net.minecraft.network.play.server.S22PacketMultiBlockChange".equals(transformedName)){
        	start(basicClass, "S22PacketMultiBlockChange");
        	///////////////////////////////////////////////
        	
        	for(MethodNode method : cn.methods) {
        		if("<init>".equals(method.name) || "S22PacketMultiBlockChange".equals(method.name) || "(I[SLapx;)V".equals(method.desc)){
        			InsnList code = method.instructions;
        			for(ListIterator<AbstractInsnNode> iterator = code.iterator(); iterator.hasNext(); ) {
        				AbstractInsnNode insn = iterator.next();
    				
        				if(insn.getOpcode() == Opcodes.ICONST_4){
        					InsnList toInsert = new InsnList();
        					toInsert.set(insn, new InsnNode(Opcodes.ICONST_5));
        					method.instructions.insert(insn, toInsert);
        					
        				}
        				if(insn.getOpcode() == Opcodes.INVOKEVIRTUAL && ((MethodInsnNode)insn).name.equals(!isObf ? "getBlock" : "a") && ((MethodInsnNode)insn).desc.equals(!isObf ? "(III)Lnet/minecraft/block/Block;" : "(III)Laji;")){
        					InsnList toInsert = new InsnList();
        					
        					insn = insn.getPrevious().getPrevious().getPrevious().getPrevious();
        					
        					for(int i = 0; i < 20; i++){
        						method.instructions.remove(insn.getPrevious());
            					insn = insn.getNext();
        					}
        					method.instructions.remove(insn.getPrevious());
        					
        					toInsert.add(new VarInsnNode(Opcodes.ALOAD, 6));
        					toInsert.add(new VarInsnNode(Opcodes.ALOAD, 3));
        					toInsert.add(new VarInsnNode(Opcodes.ILOAD, 8));
        					toInsert.add(new VarInsnNode(Opcodes.ILOAD, 10));
        					toInsert.add(new VarInsnNode(Opcodes.ILOAD, 9));
        					toInsert.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, !isObf ? "net/minecraft/world/chunk/Chunk" : "apx", !isObf ? "getBlock" : "a", !isObf ? "(III)Lnet/minecraft/block/Block;" : "(III)Laji;", false));
        					toInsert.add(new MethodInsnNode(Opcodes.INVOKESTATIC, !isObf ? "net/minecraft/block/Block" : "aji", !isObf ? "getIdFromBlock" : "b", !isObf ? "(Lnet/minecraft/block/Block;)I" : "(Laji;)I", false));
        					toInsert.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/io/DataOutputStream", "writeShort", "(I)V", false));
        					
        					toInsert.add(new VarInsnNode(Opcodes.ALOAD, 6));
        					toInsert.add(new VarInsnNode(Opcodes.ALOAD, 3));
        					toInsert.add(new VarInsnNode(Opcodes.ILOAD, 8));
        					toInsert.add(new VarInsnNode(Opcodes.ILOAD, 10));
        					toInsert.add(new VarInsnNode(Opcodes.ILOAD, 9));
        					toInsert.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, !isObf ? "net/minecraft/world/chunk/Chunk" : "apx", !isObf ? "getBlockMetadata" : "c", "(III)I", false));
        					toInsert.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/io/DataOutputStream", "writeByte", "(I)V", false));
        					
        					method.instructions.insert(insn.getPrevious(), toInsert);
        				}
        			}
        		}
        	}
        	
        	return end(basicClass, "S22PacketMultiBlockChange");
        	////////////////////////////////////////////////////
        }
        
        if("net.minecraft.network.play.server.S26PacketMapChunkBulk".equals(transformedName)){
        	start(basicClass, "S26PacketMapChunkBulk");
        	///////////////////////////////////////////
        	
        	for(MethodNode method : cn.methods) {
        		if("readPacketData".equals(method.name) || ("a".equals(method.name) && "(Let;)V".equals(method.desc))){
        			InsnList code = method.instructions;
        		
        			for(ListIterator<AbstractInsnNode> iterator = code.iterator(); iterator.hasNext(); ) {
        				AbstractInsnNode insn = iterator.next();
        				
        				if(insn.getOpcode() == Opcodes.SIPUSH && ((IntInsnNode)insn).operand == 8192){
        					
        					InsnList toInsert = new InsnList();
        					
        					toInsert.set(insn, new IntInsnNode(Opcodes.SIPUSH, 12288));
        					method.instructions.insert(toInsert);
        					break;
        				}
    				}
        		}
        	}
        	
        	return end(basicClass, "S26PacketMapChunkBulk");
        	////////////////////////////////////////////////
        }
        
        if("cofh.lib.util.helpers.BlockHelper".equals(transformedName)){
        	start(basicClass, "CoFHLib");
        	/////////////////////////////
        	
        	System.out.println("hashel class");
        	
        	for(MethodNode method : cn.methods) {
        		if("<clinit>".equals(method.name)){
        			System.out.println("hashel metod");
        			InsnList code = method.instructions;
        		
        			for(ListIterator<AbstractInsnNode> iterator = code.iterator(); iterator.hasNext(); ) {
        				AbstractInsnNode insn = iterator.next();
        				
        				if(insn.getType() == insn.INT_INSN && insn.getOpcode() == Opcodes.SIPUSH){
        					System.out.println("hashel peremennuyou");
        					InsnList toInsert = new InsnList();
        					
        					toInsert.set(insn, new LdcInsnNode(new Integer(32768)));
        					method.instructions.insert(toInsert);
        					break;
        				}
    				}
        		}
        	}
        	
        	return end(basicClass, "CoFHLib");
        	//////////////////////////////////
        }
        
        return basicClass;
	}
	
	public void start(byte[] classArray, String name){
    	cn = new ClassNode(Opcodes.ASM5);
        reader = new ClassReader(classArray);
        reader.accept(cn, 0); 
        logger.info("Patching: \"" + name + "\"");
	}
	
	public byte[] end(byte[] classArray, String name){
        writer = new ClassWriter(0);
        cn.accept(writer);
        logger.info("Patched: \"" + name + "\"");
        return writer.toByteArray();
	}
}