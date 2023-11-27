package com.gtnewhorizons.neid;

public class Constants {

    /**
     * The total number of bits used by a block's ID
     */
    public static final int BITS_PER_ID = 16;
    public static final int VANILLA_BITS_PER_ID = 12;

    /**
     * The total number of bits used by a block's metadata
     */
    public static final int BITS_PER_METADATA = 16;
    public static final int VANILLA_BITS_PER_METADATA = 4;

    public static final int METADATA_COUNT = 1 << BITS_PER_METADATA;
    public static final int VANILLA_METADATA_COUNT = 1 << VANILLA_BITS_PER_METADATA;

    /**
     * MAX_BLOCK_ID is calculated to 1 less bit than the total bits per ID to accomodate signed values. The value is
     * stored as 16 bits, but it is signed so the maximum ID we can actually use is 1 bit less. Vanilla minecraft
     * doesn't need this for the block ID because despite being a 12 bit value, it is stored in a 16-bit short.
     */
    public static final int MAX_BLOCK_ID = (1 << (BITS_PER_ID - 1)) - 1;
    public static final int VANILLA_MAX_BLOCK_ID = (1 << VANILLA_BITS_PER_ID) - 1;

    /**
     * This is the same thing as the max block ID in vanilla, since vanilla does not use up a bit for signed numbers.
     * For us it is 1 bit larger than the max block ID, but for vanilla it is identical.
     */
    public static final int BLOCK_ID_MASK = (1 << BITS_PER_ID) - 1;
    public static final int VANILLA_BLOCK_ID_MASK = VANILLA_MAX_BLOCK_ID;

    public static final int METADATA_MASK = (1 << BITS_PER_METADATA) - 1;
    public static final int VANILLA_METADATA_MASK = (1 << VANILLA_BITS_PER_METADATA) - 1;

    /**
     * Number of block stored in a single EBS, this is the same as vanilla.
     */
    public static final int BLOCKS_PER_EBS = 4096;

    /**
     * This number is the total bytes in an ExtendedBlockStorage. It is: LSB + MSB + Metadata + Skylight Data +
     * Blocklight Data. In vanilla: 8 + 4 + 4 + 4 + 4 = 24 bits per block = 3 bytes per block * 4,096 blocks per EBS =
     * 12288 bytes per EBS Our equation: 16 + 0 + 16 + 4 + 4 = 40 bits per block = 5 bytes per block * 4,096 blocks per
     * EBS = 20480 bytes per EBS
     */
    public static final int BYTES_PER_EBS = 20480;
    public static final int VANILLA_BYTES_PER_EBS = 12288;

    /**
     * This number is the total bytes stored in a chunk. It is: Number of EBS in a chunk(16) * BYTES_PER_EBS +
     * MAX_BIOME_ID(256) In vanilla: 16 * 12288 + 256 = 196864 Our equation: 16 * BYTES_PER_EBS(20480) + 256 = 327936
     */
    public static final int BYTES_PER_CHUNK = 327936;
    public static final int VANILLA_BYTES_PER_CHUNK = 196864;

    /**
     * This number is the total bytes stored in an EBS, minute the lighting data. It is: (LSB + MSB + Metadata) *
     * BLOCKS_PER_EBS(4096) In vanilla: 8 + 4 + 4 = 16 bits per block = 2 bytes per block * 4096 = 8192 Our equation: 16
     * + 0 + 16 = 20 bits per block = 4 bytes per block * 4096 = 16384.
     *
     * If you look at vanilla source code, this value will be 2048 because seemingly Vanilla handles less EBS per chunk?
     * Unsure, but Forge ASM's this value to 8192, so that is what we are looking for to modify.
     */
    public static final int BYTES_PER_EBS_MINUS_LIGHTING = 16384;
    public static final int VANILLA_BYTES_PER_EBS_MINUS_LIGHTING = 8192;

    public static final int MAX_DATA_WATCHER_ID = 127;
}
