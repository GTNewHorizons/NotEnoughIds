package ru.fewizz.idextender;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.chunk.NibbleArray;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;


public class Hooks {
	public static final int NUM_BLOCK_IDS = getNumBlockIDs();
	public static final int NUM_ITEM_IDS = getNumItemIDs();

	private static int getNumBlockIDs() {
		return 32768;
	}

	private static int getNumItemIDs() {
		return 32768;
	}

	public static byte[] rotateType = new byte[32768];
	public static byte bite[] = new byte[4];
	public static final boolean USING_16BIT_BLOCK_IDS = NUM_BLOCK_IDS > 4096;

	public static void writeChunkToNbt(ExtendedBlockStorage ebs, NBTTagCompound nbt) {
		if (USING_16BIT_BLOCK_IDS)
			nbt.setByteArray("Blocks16", get16BitBlockArray(ebs));
		else {
			nbt.setByteArray("Blocks", ebs.getBlockLSBArray());

			if (ebs.getBlockMSBArray() != null) {
				nbt.setByteArray("Add", ebs.getBlockMSBArray().data);
			}
		}
	}

	public static void readChunkFromNbt(ExtendedBlockStorage ebs, NBTTagCompound nbt) {
		if (USING_16BIT_BLOCK_IDS)
			set16BitBlockArray(nbt.getByteArray("Blocks16"), ebs);
		else {
			ebs.setBlockLSBArray(nbt.getByteArray("Blocks"));

			if (nbt.hasKey("Add", 7)) {
				ebs.setBlockMSBArray(new NibbleArray(nbt.getByteArray("Add"), 4));
			}
		}
	}

	public static void ifUsingShort(boolean b) {
		if (b && USING_16BIT_BLOCK_IDS)
			throw new UnsupportedOperationException("Cannot access 12-bit arrays in 16-bit mode");
		if (!b && USING_16BIT_BLOCK_IDS) return;
		if (b && !USING_16BIT_BLOCK_IDS) return;
		if (!b && !USING_16BIT_BLOCK_IDS)
			throw new UnsupportedOperationException("Cannot access 12-bit arrays in 16-bit mode");
	}

	public static boolean ifUsingShortBoolean(boolean b) {
		if (b && USING_16BIT_BLOCK_IDS) return true;
		if (!b && USING_16BIT_BLOCK_IDS) return false;
		if (b && !USING_16BIT_BLOCK_IDS) return false;
		if (!b && !USING_16BIT_BLOCK_IDS) return true;
		return false;
	}

	public static void set16BitBlockArray(byte[] byteArray, ExtendedBlockStorage ebs) {
		Hooks.ifUsingShort(false);
		short[] array = get(ebs);
		for (int k = 0; k < byteArray.length; k += 2)
			array[k / 2] = (short) ((byteArray[k] << 8) | (byteArray[k + 1] & 255));
	}

	public static byte[] get16BitBlockArray(ExtendedBlockStorage ebs) {
		Hooks.ifUsingShort(false);
		byte[] b = new byte[8192];
		short[] array = get(ebs);
		for (int k = 0; k < 8192; k += 2) {
			b[k] = (byte) (array[k / 2] >> 8);
			b[k + 1] = (byte) array[k / 2];
		}
		return b;
	}

	public static Block getBlockById(int i1, int i2, int i3, ExtendedBlockStorage ebs) {
		return Block.getBlockById(get(ebs)[i2 << 8 | i3 << 4 | i1] & 65535);
	}

	public static void setBlock(int id, int x, int y, int z, ExtendedBlockStorage ebs) {
		if (USING_16BIT_BLOCK_IDS) {
			get(ebs)[y << 8 | z << 4 | x] = (short) id;
			return;
		}
	}

	public static short[] create16BArray() {
		return new short[4096];
	}

	public static Block Void() {
		return null;
	}

	public static int zero() {
		return 0;
	}

	public static short[] get(ExtendedBlockStorage ebs) {
		return null;
	}

	public static int setID(ExtendedBlockStorage ebs, int x, int y, int z) {
		return get(ebs)[y << 8 | z << 4 | x];
	}
}
