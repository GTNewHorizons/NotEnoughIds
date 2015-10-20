package ru.fewizz.idextender.asm;

public enum Name {
	// self
	hooks("ru/fewizz/idextender/Hooks"),

	hooks_getBlockData("getBlockData", null, "(Lnet/minecraft/world/chunk/storage/ExtendedBlockStorage;)[B"),
	hooks_setBlockData("setBlockData", null, "(Lnet/minecraft/world/chunk/storage/ExtendedBlockStorage;[BI)V"),
	hooks_writeChunkToNbt("writeChunkToNbt", null, "(Lnet/minecraft/nbt/NBTTagCompound;Lnet/minecraft/world/chunk/storage/ExtendedBlockStorage;)V"),
	hooks_readChunkFromNbt("readChunkFromNbt", null, "(Lnet/minecraft/world/chunk/storage/ExtendedBlockStorage;Lnet/minecraft/nbt/NBTTagCompound;)V"),
	hooks_getBlockId("getBlockId", null, "(Lnet/minecraft/world/chunk/storage/ExtendedBlockStorage;III)I"),
	hooks_getBlockById("getBlock", null, "(Lnet/minecraft/world/chunk/storage/ExtendedBlockStorage;III)Lnet/minecraft/block/Block;"),
	hooks_setBlockId("setBlockId", null, "(Lnet/minecraft/world/chunk/storage/ExtendedBlockStorage;IIII)V"),

	// vanilla
	block("net/minecraft/block/Block"),
	extendedBlockStorage("net/minecraft/world/chunk/storage/ExtendedBlockStorage"),
	iChunkProvider("net/minecraft/world/chunk/IChunkProvider"),
	nibbleArray("net/minecraft/world/chunk/NibbleArray"),
	world("net/minecraft/world/World"),

	ebs_getBlock("getBlockByExtId", "func_150819_a", "(III)Lnet/minecraft/block/Block;"), // ExtendedBlockStorage
	ebs_setBlock("func_150818_a", null, "(IIILnet/minecraft/block/Block;)V"),
	ebs_getBlockLSBArray("getBlockLSBArray", "func_76658_g", "()[B"),
	ebs_getBlockMSBArray("getBlockMSBArray", "func_76660_i", "()Lnet/minecraft/world/chunk/NibbleArray;"),
	ebs_setBlockMSBArray("setBlockMSBArray", "func_76673_a", "(Lnet/minecraft/world/chunk/NibbleArray;)V"),
	ebs_isEmpty("isEmpty", "func_76663_a", "()Z"),
	ebs_removeInvalidBlocks("removeInvalidBlocks", "func_76672_e", "()V"),

	acl_writeChunkToNBT("writeChunkToNBT", "func_75820_a", "(Lnet/minecraft/world/chunk/Chunk;Lnet/minecraft/world/World;Lnet/minecraft/nbt/NBTTagCompound;)V"), // AnvilChunkLoader
	acl_readChunkFromNBT("readChunkFromNBT", "func_75823_a", "(Lnet/minecraft/world/World;Lnet/minecraft/nbt/NBTTagCompound;)Lnet/minecraft/world/chunk/Chunk;"),

	block_getIdFromBlock("getIdFromBlock", "func_149682_b", "(Lnet/minecraft/block/Block;)I"),
	chunk_fillChunk("fillChunk", "func_76607_a", "([BIIZ)V"),

	packet_readPacketData("readPacketData", "func_148837_a", "(Lnet/minecraft/network/PacketBuffer;)V"),
	nhpc_handleMultiBlockChange("handleMultiBlockChange", "func_147287_a", "(Lnet/minecraft/network/play/server/S22PacketMultiBlockChange;)V"), // NetHandlerPlayClient
	s22_init_server("<init>", null, "(I[SLnet/minecraft/world/chunk/Chunk;)V"), // S22PacketMultiBlockChange

	// underground biomes

	ub_bud_replaceChunkOres_world("replaceChunkOres", null, "(IILnet/minecraft/world/World;)V"),
	ub_bud_replaceChunkOres_iChunkProvider("replaceChunkOres", null, "(Lnet/minecraft/world/chunk/IChunkProvider;II)V"); // BiomeUndergroundDecorator

	private Name(String deobf) {
		this.deobf = deobf;
		this.obf = deobf;
		this.desc = null;
	}

	private Name(String deobf, String obf, String desc) {
		this.deobf = deobf;
		this.obf = obf != null ? obf : deobf;
		this.desc = desc;
	}

	public String get(boolean obfuscated) {
		return obfuscated ? obf : deobf;
	}

	public String getDesc(boolean obfuscated) {
		return desc;
	}

	@SuppressWarnings("unused")
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

	public final String deobf;
	public final String obf;
	public final String desc;
	public String obfDesc;

	static {
		//translateDescs(); - not needed for srg env/names
	}
}
