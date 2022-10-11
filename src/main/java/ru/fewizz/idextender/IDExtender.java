package ru.fewizz.idextender;

import cpw.mods.fml.common.*;
import cpw.mods.fml.common.event.*;

@Mod(modid = IDExtender.MODID, name = IDExtender.NAME, version = IDExtender.VERSION)
public class IDExtender
{
    public static final String MODID = "GRADLETOKEN_MODID";
    public static final String VERSION = "GRADLETOKEN_VERSION";
    public static final String NAME = "GRADLETOKEN_MODNAME";
    
    @Mod.EventHandler
    public void test(final FMLPreInitializationEvent event) {
    }
}
