package ru.fewizz.idextender;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;


@Mod(modid = "notenoughIDs", name = "NotEnoughIDs", version = "1.0.0")
public class IDExtender {
	@EventHandler
	public void test(FMLPreInitializationEvent event) {
		// for(int i = 0; i < 4500; i++){
		// Block shield = new BlockIce().setBlockName(".shield" + i);
		// GameRegistry.registerBlock(shield, i + "ShieldBlock");
		// //System.out.println("TUTA");
		// shield.setCreativeTab(CreativeTabs.tabFood);
		// }
	}
}
