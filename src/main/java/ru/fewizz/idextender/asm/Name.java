package ru.fewizz.idextender.asm;

public enum Name {
	block("net/minecraft/block/Block", "aji"),
	nibbleArray("net/minecraft/world/chunk/NibbleArray", "apv"),
	extendedBlockStorage("net/minecraft/world/chunk/storage/ExtendedBlockStorage", "apz"),

	ebs_getBlock("getBlockByExtId", "a", "(III)Lnet/minecraft/block/Block;"), // ExtendedBlockStorage
	ebs_setBlock("func_150818_a", "a", "(IIILnet/minecraft/block/Block;)V"),
	ebs_getBlockMSBArray("getBlockMSBArray", "i", "()Lnet/minecraft/world/chunk/NibbleArray;"),
	ebs_isEmpty("isEmpty", "a", "()Z"),
	ebs_removeInvalidBlocks("removeInvalidBlocks", "e", "()V");

	private Name(String deobf, String obf) {
		this.deobf = deobf;
		this.obf = obf;
		this.desc = null;
	}

	private Name(String deobf, String obf, String desc) {
		this.deobf = deobf;
		this.obf = obf;
		this.desc = desc;
	}

	public String get(boolean obfuscated) {
		return obfuscated ? obf : deobf;
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

	public final String deobf;
	public final String obf;
	public final String desc;
	public String obfDesc;

	static {
		translateDescs();
	}
}
