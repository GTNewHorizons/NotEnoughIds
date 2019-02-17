package ru.fewizz.idextender;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBookshelf;
import net.minecraft.block.BlockIce;
import net.minecraft.block.BlockSand;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.creativetab.CreativeTabs;

@Mod(
		modid = "notenoughIDs",
		name = "NotEnoughIDs",
		version = "1.4.3.5")
public class IDExtender {

	static final boolean DEBUG = Boolean.parseBoolean(System.getProperty("neid.debug", "false"));

	@EventHandler
	public void test(FMLPreInitializationEvent event) {
		if(!DEBUG)
			return;
		FMLCommonHandler.instance().bus().register(this);
		for (int i = 0; i < 4500; i++) {
			Block shield = new BlockIce().setBlockName(".shield" + i);
			shield.setCreativeTab(CreativeTabs.tabFood);
			GameRegistry.registerBlock(shield, i + "ShieldBlock");
		}
		
		Block ore = new BlockBookshelf().setBlockName("ore");
		ore.setCreativeTab(CreativeTabs.tabFood);
		GameRegistry.registerBlock(ore, "ore");
		
		Block sand = new BlockSand().setBlockName("sand");
		sand.setCreativeTab(CreativeTabs.tabFood);
		GameRegistry.registerBlock(sand, "sand");
		
	}

}
