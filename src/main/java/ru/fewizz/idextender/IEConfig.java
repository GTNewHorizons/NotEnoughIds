package ru.fewizz.idextender;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class IEConfig {
	static Configuration config;
	public static boolean removeInvalidBlocks = false;
	public static boolean oldWorldsSupport = true;
	public static boolean extendDataWatcher = true;
	
	public static void init(File file){
		File newFile;
		if(file.getPath().contains("bin"))
			newFile = new File(file.getParent() + "/eclipse/config", "NEID.txt");
		else
			newFile = new File(file.getParentFile().getParent() + "/config", "NEID.txt");
		config = new Configuration(newFile);
		
		removeInvalidBlocks = config.getBoolean("RemoveInvalidBlocks", "NEID", false, "");
		oldWorldsSupport = config.getBoolean("OldWorldsSupport", "NEID", true, "");
		extendDataWatcher = config.getBoolean("ExtendDataWatcher", "NEID", false, "");
		
		config.save();
	}
}
