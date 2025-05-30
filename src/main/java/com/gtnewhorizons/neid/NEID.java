package com.gtnewhorizons.neid;

import com.gtnewhorizon.gtnhlib.config.ConfigException;
import com.gtnewhorizon.gtnhlib.config.ConfigurationManager;

import com.gtnewhorizons.foundation.HandlerRegistry;
import com.gtnewhorizons.neid.handlers.IDHandler;
import com.gtnewhorizons.neid.handlers.MetadataHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(
        modid = "neid",
        name = "NotEnoughIDs",
        version = Tags.VERSION,
        dependencies = "after:battlegear2@[1.3.0,);" + " required-after:gtnhlib@[0.6.18,);")
public class NEID {

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        try {
            ConfigurationManager.registerConfig(NEIDConfig.class);
        } catch (ConfigException e) {
            throw new RuntimeException("Failed to register NotEnoughIDs config!");
        }

        IDHandler idHandler = new IDHandler();
        MetadataHandler metadataHandler = new MetadataHandler();

        HandlerRegistry.registerChunkPacketHandler(idHandler);
        HandlerRegistry.registerChunkPacketHandler(metadataHandler);
        HandlerRegistry.registerBlockPacketHandler(metadataHandler);
    }

}
