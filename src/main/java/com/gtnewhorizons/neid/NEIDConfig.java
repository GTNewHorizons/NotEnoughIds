package com.gtnewhorizons.neid;

import com.gtnewhorizon.gtnhlib.config.Config;

@Config(modid = "neid", category = "neid")
public class NEIDConfig {

    @Config.Comment("Causes a crash when a block has not been registered(e.g. has an id of -1)")
    public static boolean CatchUnregisteredBlocks = false;

    @Config.Comment("Remove invalid (corrupted) blocks from the game.")
    public static boolean RemoveInvalidBlocks = false;

    @Config.Comment("If true, only blocks with IDs > 4095 will disappear after removing NEID. Metadatas outside of the range 0-15 will be set to 0.")
    public static boolean PostNeidWorldsSupport = true;

    @Config.Comment("Extend DataWatch IDs. Vanilla limit is 31, new limit is 127.")
    public static boolean ExtendDataWatcher = false;

}
