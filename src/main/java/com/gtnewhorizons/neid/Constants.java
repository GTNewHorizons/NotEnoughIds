package com.gtnewhorizons.neid;

public class Constants {

    public static final int BITS_PER_ID = 16;
    public static final int BITS_PER_METADATA = 4;
    // MAX_BLOCK_ID is calculated to 1 less bit than the total bits per ID
    // to accomodate signed values. The value is stored as 16 bits, but
    // it is signed so the maximum ID we can actually use is 1 bit less.
    // Vanilla minecraft doesn't need this for the block ID because despite
    // being a 12 bit value, it is stored in a 16-bit short.
    public static final int MAX_BLOCK_ID = (1 << (BITS_PER_ID - 1)) - 1;
    public static final int BLOCK_ID_MASK = (1 << BITS_PER_ID) - 1;
    public static final int ebsIdArrayLength = 4096;
    public static final int ebsIdArraySize = 8192;
    public static final int vanillaEbsSize = 12288;
    public static final int vanillaSize = 196864;
    public static final int newEbsSize = 14336;
    public static final int newSize = 229632;
    public static final int maxDataWatcherId = 127;
}
