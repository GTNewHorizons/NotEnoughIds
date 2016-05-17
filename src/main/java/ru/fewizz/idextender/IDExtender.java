package ru.fewizz.idextender;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBookshelf;
import net.minecraft.block.BlockIce;
import net.minecraft.block.BlockOre;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockStone;
import net.minecraft.creativetab.CreativeTabs;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(
		modid = "notenoughIDs",
		name = "NotEnoughIDs",
		version = "1.4.3.3")
public class IDExtender {

	@EventHandler
	public void test(FMLPreInitializationEvent event) {
		//FMLCommonHandler.instance().bus().register(this);
		//for (int i = 0; i < 4500; i++) {
		//	Block shield = new BlockIce().setBlockName(".shield" + i);
		//	shield.setCreativeTab(CreativeTabs.tabFood);
		//	GameRegistry.registerBlock(shield, i + "ShieldBlock");
		//}
		
		//Block ore = new BlockBookshelf().setBlockName("ore");
		//ore.setCreativeTab(CreativeTabs.tabFood);
		//GameRegistry.registerBlock(ore, "ore");
		
		//Block sand = new BlockSand().setBlockName("sand");
		//sand.setCreativeTab(CreativeTabs.tabFood);
		//GameRegistry.registerBlock(sand, "sand");
	}

	
	//@SubscribeEvent
	//public void setBlock(TickEvent.WorldTickEvent e) {
	//	boolean setunregisteredBlock = false; // Controlling only in debug mode
	//	if (setunregisteredBlock) {
	//		System.out.println("Setting unregistered block");
	//		EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
	//		e.world.setBlock((int) player.posX, (int) player.posY - 2, (int) player.posZ, new BlockIce());
	//	}
	//}

}
