package ru.fewizz.idextender.Tweaker;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.relauncher.CoreModManager;
import ru.fewizz.idextender.IETransformer;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;

public class IETweaker implements ITweaker{

	LaunchClassLoader lcl;
	public Logger logger = LogManager.getLogger("NEID");
	
	@Override
	public void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile) {
	}

	@Override
	public void injectIntoClassLoader(LaunchClassLoader classLoader) {
		lcl = classLoader;
		
		Field f;
		Field f2;
		
		Set<String> transformerExceptions;
		Set<String> classLoaderExceptions;
		
		try {
			(f = LaunchClassLoader.class.getDeclaredField("transformerExceptions")).setAccessible(true);
			(f2 = LaunchClassLoader.class.getDeclaredField("classLoaderExceptions")).setAccessible(true);
			try {
				transformerExceptions = (Set<String>) f.get(lcl);
				classLoaderExceptions = (Set<String>) f2.get(lcl);
				transformerExceptions.remove("fastcraft");
				f.set(lcl, transformerExceptions);
				f2.set(lcl, classLoaderExceptions);
				try {
					lcl.loadClass("fastcraft.r");
					logger.info("Fastcraft compatibility enabled.");
				}
				catch (ClassNotFoundException e) {}
			} 
			catch (IllegalArgumentException e) {e.printStackTrace();} 
			catch (IllegalAccessException e) {e.printStackTrace();}	
		} 
		catch (SecurityException e) {e.printStackTrace();} 
		catch (NoSuchFieldException e) {e.printStackTrace();}
	}

	@Override
	public String getLaunchTarget() {
		throw new IllegalStateException();
	}

	@Override
	public String[] getLaunchArguments() {
	    return new String[0];
	}
	
}
