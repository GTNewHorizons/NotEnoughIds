package com.gtnewhorizons.neid;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = NEID.MODID, name = NEID.NAME, version = NEID.VERSION)
public class NEID {

    public static final String MODID = "GRADLETOKEN_MODID";
    public static final String VERSION = "GRADLETOKEN_VERSION";
    public static final String NAME = "GRADLETOKEN_MODNAME";

    @Mod.EventHandler
    public void test(final FMLPreInitializationEvent event) {}
}
