package com.gtnewhorizons.neid;

import java.io.File;

import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.common.config.Configuration;

public class NEIDConfig {

    static Configuration config;
    public static boolean catchUnregisteredBlocks;
    public static boolean removeInvalidBlocks;
    public static boolean postNeidWorldsSupport;
    public static boolean extendDataWatcher;

    static {
        NEIDConfig.config = new Configuration(new File(Launch.minecraftHome, "config/NEID.cfg"));
        NEIDConfig.catchUnregisteredBlocks = NEIDConfig.config.getBoolean("CatchUnregisteredBlocks", "NEID", false, "");
        NEIDConfig.removeInvalidBlocks = NEIDConfig.config
                .getBoolean("RemoveInvalidBlocks", "NEID", false, "Remove invalid (corrupted) blocks from the game.");
        NEIDConfig.postNeidWorldsSupport = NEIDConfig.config.getBoolean(
                "PostNeidWorldsSupport",
                "NEID",
                true,
                "If true, only blocks with IDs > 4095 will disappear after removing NEID.");
        NEIDConfig.extendDataWatcher = NEIDConfig.config.getBoolean(
                "ExtendDataWatcher",
                "NEID",
                false,
                "Extend DataWatcher IDs. Vanilla limit is 31, new limit is 127.");
        NEIDConfig.config.save();
    }
}
