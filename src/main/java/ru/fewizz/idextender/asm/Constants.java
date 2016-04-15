package ru.fewizz.idextender.asm;

public class Constants {
	public static final int maxBlockId = 32767;
	public static final int blockIdMask = 0xffff; // 65535

	public static final int ebsIdArrayLength = 4096;
	public static final int ebsIdArraySize = ebsIdArrayLength * 2;

	public static final int vanillaEbsSize = 12288; // without msb
	public static final int vanillaSize = 196864;
	public static final int newEbsSize = ebsIdArraySize + 3 * ebsIdArrayLength / 2; // ids + 3 * nibble (meta, blocklight, skylight)
	public static final int newSize = 16 * newEbsSize + 256; // ebsCount * ebsSize + biome
	
	public static final int maxDataWatcherId = 127;
}
