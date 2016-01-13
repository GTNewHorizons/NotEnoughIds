package ru.fewizz.idextender.asm;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public enum Name {
	// self
	hooks("ru/fewizz/idextender/Hooks"),

	hooks_create16BArray(hooks, "create16BArray", null, null, "()[S"),
	hooks_getBlockData(hooks, "getBlockData", null, null, "(Lnet/minecraft/world/chunk/storage/ExtendedBlockStorage;)[B"),
	hooks_setBlockData(hooks, "setBlockData", null, null, "(Lnet/minecraft/world/chunk/storage/ExtendedBlockStorage;[BI)V"),
	hooks_writeChunkToNbt(hooks, "writeChunkToNbt", null, null, "(Lnet/minecraft/nbt/NBTTagCompound;Lnet/minecraft/world/chunk/storage/ExtendedBlockStorage;)V"),
	hooks_readChunkFromNbt(hooks, "readChunkFromNbt", null, null, "(Lnet/minecraft/world/chunk/storage/ExtendedBlockStorage;Lnet/minecraft/nbt/NBTTagCompound;)V"),
	hooks_getBlockId(hooks, "getBlockId", null, null, "(Lnet/minecraft/world/chunk/storage/ExtendedBlockStorage;III)I"),
	hooks_getBlockById(hooks, "getBlock", null, null, "(Lnet/minecraft/world/chunk/storage/ExtendedBlockStorage;III)Lnet/minecraft/block/Block;"),
	hooks_setBlockId(hooks, "setBlockId", null, null, "(Lnet/minecraft/world/chunk/storage/ExtendedBlockStorage;IIII)V"),
	hooks_getIdFromBlockWithCheck(hooks, "getIdFromBlockWithCheck", null, null, "(Lnet/minecraft/block/Block;)I"),

	// vanilla
	acl("net/minecraft/world/chunk/storage/AnvilChunkLoader", "aqk"),
	block("net/minecraft/block/Block", "aji"),
	chunk("net/minecraft/world/chunk/Chunk", "apx"),
	extendedBlockStorage("net/minecraft/world/chunk/storage/ExtendedBlockStorage", "apz"),
	iChunkProvider("net/minecraft/world/chunk/IChunkProvider", "apu"),
	nbtTagCompound("net/minecraft/nbt/NBTTagCompound", "dh"),
	nhpc("net/minecraft/client/network/NetHandlerPlayClient", "bjb"),
	nibbleArray("net/minecraft/world/chunk/NibbleArray", "apv"),
	packet("net/minecraft/network/Packet", "ft"),
	packetBuffer("net/minecraft/network/PacketBuffer", "et"),
	s22("net/minecraft/network/play/server/S22PacketMultiBlockChange", "gk"),
	s21("net/minecraft/network/play/server/S21PacketChunkData", "gx"),
	s21_extracted("net/minecraft/network/play/server/S21PacketChunkData$Extracted", "gy"),
	world("net/minecraft/world/World", "ahb"),
	dataWatcher("net/minecraft/entity/DataWatcher", "te"),
	dataWatcher_watchableObject("net/minecraft/entity/DataWatcher$WatchableObject", "tf"),

	ebs_getBlock(extendedBlockStorage, "getBlockByExtId", "a", "func_150819_a", "(III)Lnet/minecraft/block/Block;"), // ExtendedBlockStorage
	ebs_setBlock(extendedBlockStorage, "func_150818_a", "a", null, "(IIILnet/minecraft/block/Block;)V"),
	ebs_getBlockLSBArray(extendedBlockStorage, "getBlockLSBArray", "g", "func_76658_g", "()[B"),
	ebs_getBlockMSBArray(extendedBlockStorage, "getBlockMSBArray", "i", "func_76660_i", "()Lnet/minecraft/world/chunk/NibbleArray;"),
	ebs_setBlockMSBArray(extendedBlockStorage, "setBlockMSBArray", "a", "func_76673_a", "(Lnet/minecraft/world/chunk/NibbleArray;)V"),
	ebs_isEmpty(extendedBlockStorage, "isEmpty", "a", "func_76663_a", "()Z"),
	ebs_removeInvalidBlocks(extendedBlockStorage, "removeInvalidBlocks", "e", "func_76672_e", "()V"),
	dataWatcher_addObject(dataWatcher, "addObject", "a", "func_75682_a", "(ILjava/lang/Object;)V"),
	dataWatcher_writeWatchableObjectToPacketBuffer(dataWatcher, "writeWatchableObjectToPacketBuffer", "a", "func_151510_a", "(Lnet/minecraft/network/PacketBuffer;Lnet/minecraft/entity/DataWatcher$WatchableObject;)V"),
	dataWatcher_readWatchedListFromPacketBuffer(dataWatcher, "readWatchedListFromPacketBuffer", "b", "func_151508_b", "(Lnet/minecraft/network/PacketBuffer;)Ljava/util/List;"),
	dataWatcher_writeWatchedListToPacketBuffer(dataWatcher, "writeWatchedListToPacketBuffer", "a", "func_151507_a", "(Ljava/util/List;Lnet/minecraft/network/PacketBuffer;)V"),
	dataWatcher_func_151509_a(dataWatcher, "func_151509_a", "a", "func_151509_a", "(Lnet/minecraft/network/PacketBuffer;)V"),
	ebs_blockRefCount(extendedBlockStorage, "blockRefCount", "b", "field_76682_b", "I"),
	ebs_tickRefCount(extendedBlockStorage, "tickRefCount", "c", "field_76683_c", "I"),
	acl_writeChunkToNBT(acl, "writeChunkToNBT", "a", "func_75820_a", "(Lnet/minecraft/world/chunk/Chunk;Lnet/minecraft/world/World;Lnet/minecraft/nbt/NBTTagCompound;)V"), // AnvilChunkLoader
	acl_readChunkFromNBT(acl, "readChunkFromNBT", "a", "func_75823_a", "(Lnet/minecraft/world/World;Lnet/minecraft/nbt/NBTTagCompound;)Lnet/minecraft/world/chunk/Chunk;"),
	block_getIdFromBlock(block, "getIdFromBlock", "b", "func_149682_b", "(Lnet/minecraft/block/Block;)I"),
	chunk_fillChunk(chunk, "fillChunk", "a", "func_76607_a", "([BIIZ)V"),
	packet_readPacketData(packet, "readPacketData", "a", "func_148837_a", "(Lnet/minecraft/network/PacketBuffer;)V"),
	nhpc_handleMultiBlockChange(nhpc, "handleMultiBlockChange", "a", "func_147287_a", "(Lnet/minecraft/network/play/server/S22PacketMultiBlockChange;)V"), // NetHandlerPlayClient
	s22_init_server(s22, "<init>", null, null, "(I[SLnet/minecraft/world/chunk/Chunk;)V"), // S22PacketMultiBlockChange
	s21_undefined1(s21, "func_149275_c", "c", null, "()I"),
	s21_undefined2(s21, "func_149269_a", "a", null, "(Lnet/minecraft/world/chunk/Chunk;ZI)Lnet/minecraft/network/play/server/S21PacketChunkData$Extracted;"),

	// underground biomes
	ub_bud("exterminatorJeff/undergroundBiomes/worldGen/BiomeUndergroundDecorator"),

	ub_bud_replaceChunkOres_world(ub_bud, "replaceChunkOres", null, null, "(IILnet/minecraft/world/World;)V"),
	ub_bud_replaceChunkOres_iChunkProvider(ub_bud, "replaceChunkOres", null, null, "(Lnet/minecraft/world/chunk/IChunkProvider;II)V"), // BiomeUndergroundDecorator
	
	// MFQM
	MFQM("MoreFunQuicksandMod/main/MFQM"),
	
	MFQM_preInit(MFQM, "preInit", null, null, "(Lcpw/mods/fml/common/event/FMLPreInitializationEvent;)V");

	// for non-mc classes
	private Name(String deobf) {
		this(deobf, deobf);
	}

	// for mc classes
	private Name(String deobf, String obf) {
		this.clazz = null;
		this.deobf = deobf;
		this.obf = obf;
		this.srg = deobf;
		this.desc = null;
	}

	// for fields and methods
	private Name(Name clazz, String deobf, String obf, String srg, String desc) {
		this.clazz = clazz;
		this.deobf = deobf;
		this.obf = obf != null ? obf : deobf;
		this.srg = srg != null ? srg : deobf;
		this.desc = desc;
	}

	public boolean matches(MethodNode x) {
		assert desc.startsWith("(");

		return obf.equals(x.name) && obfDesc.equals(x.desc) ||
				srg.equals(x.name) && desc.equals(x.desc) ||
				deobf.equals(x.name) && desc.equals(x.desc);
	}

	public boolean matches(FieldNode x) {
		assert !desc.startsWith("(");

		return obf.equals(x.name) && obfDesc.equals(x.desc) ||
				srg.equals(x.name) && desc.equals(x.desc) ||
				deobf.equals(x.name) && desc.equals(x.desc);
	}

	public boolean matches(MethodInsnNode x, boolean obfuscated) {
		assert desc.startsWith("(");

		if (obfuscated) {
			return clazz.obf.equals(x.owner) && obf.equals(x.name) && obfDesc.equals(x.desc) ||
					clazz.srg.equals(x.owner) && srg.equals(x.name) && desc.equals(x.desc);
		} else {
			return clazz.deobf.equals(x.owner) && deobf.equals(x.name) && desc.equals(x.desc);
		}
	}

	public boolean matches(FieldInsnNode x, boolean obfuscated) {
		assert !desc.startsWith("(");

		if (obfuscated) {
			return clazz.obf.equals(x.owner) && obf.equals(x.name) && obfDesc.equals(x.desc) ||
					clazz.srg.equals(x.owner) && srg.equals(x.name) && desc.equals(x.desc);
		} else {
			return clazz.deobf.equals(x.owner) && deobf.equals(x.name) && desc.equals(x.desc);
		}
	}

	public MethodInsnNode staticInvocation(boolean obfuscated) {
		// static interface methods aren't supported by this, they'd need itf=true
		assert desc.startsWith("(");

		if (obfuscated) { // srg invocation
			return new MethodInsnNode(Opcodes.INVOKESTATIC, clazz.srg, srg, desc, false);
		} else {
			return new MethodInsnNode(Opcodes.INVOKESTATIC, clazz.deobf, deobf, desc, false);
		}
	}

	public MethodInsnNode virtualInvocation(boolean obfuscated) {
		assert desc.startsWith("(");

		if (obfuscated) { // srg invocation
			return new MethodInsnNode(Opcodes.INVOKEVIRTUAL, clazz.srg, srg, desc, false);
		} else {
			return new MethodInsnNode(Opcodes.INVOKEVIRTUAL, clazz.deobf, deobf, desc, false);
		}
	}

	public FieldInsnNode staticGet(boolean obfuscated) {
		assert !desc.startsWith("(");

		if (obfuscated) { // srg access
			return new FieldInsnNode(Opcodes.GETSTATIC, clazz.srg, srg, desc);
		} else {
			return new FieldInsnNode(Opcodes.GETSTATIC, clazz.deobf, deobf, desc);
		}
	}

	public FieldInsnNode virtualGet(boolean obfuscated) {
		assert !desc.startsWith("(");

		if (obfuscated) { // srg access
			return new FieldInsnNode(Opcodes.GETFIELD, clazz.srg, srg, desc);
		} else {
			return new FieldInsnNode(Opcodes.GETFIELD, clazz.deobf, deobf, desc);
		}
	}

	public FieldInsnNode staticSet(boolean obfuscated) {
		assert !desc.startsWith("(");

		if (obfuscated) { // srg access
			return new FieldInsnNode(Opcodes.PUTSTATIC, clazz.srg, srg, desc);
		} else {
			return new FieldInsnNode(Opcodes.PUTSTATIC, clazz.deobf, deobf, desc);
		}
	}

	public FieldInsnNode virtualSet(boolean obfuscated) {
		assert !desc.startsWith("(");

		if (obfuscated) { // srg access
			return new FieldInsnNode(Opcodes.PUTFIELD, clazz.srg, srg, desc);
		} else {
			return new FieldInsnNode(Opcodes.PUTFIELD, clazz.deobf, deobf, desc);
		}
	}

	private static void translateDescs() {
		StringBuilder sb = new StringBuilder();

		for (Name name : Name.values()) {
			if (name.desc == null) continue;

			int pos = 0;
			int endPos = -1;

			while ((pos = name.desc.indexOf('L', pos)) != -1) {
				sb.append(name.desc, endPos + 1, pos);
				endPos = name.desc.indexOf(';', pos + 1);

				String cName = name.desc.substring(pos + 1, endPos);

				for (Name name2 : Name.values()) {
					if (name2.deobf.equals(cName)) {
						cName = name2.obf;
						break;
					}
				}

				sb.append('L');
				sb.append(cName);
				sb.append(';');

				pos = endPos + 1;
			}

			sb.append(name.desc, endPos + 1, name.desc.length());

			name.obfDesc = sb.toString();
			sb.setLength(0);

			//System.out.printf("deobf: %s, obf: %s, desc: %s, obfDesc: %s%n", name.deobf, name.obf, name.desc, name.obfDesc);
		}
	}

	public final Name clazz;
	public final String deobf;
	public final String obf;
	public final String srg;
	public final String desc;
	public String obfDesc;

	static {
		translateDescs();
	}
}
