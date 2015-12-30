package ru.fewizz.idextender.asm;

import java.util.HashMap;
import java.util.Map;

import ru.fewizz.idextender.asm.transformer.*;

public enum ClassEdit {
	SelfHooks(new SelfHooks(), "ru.fewizz.idextender.Hooks"),

	VanillaBlockFire(new VanillaBlockFire(), "net.minecraft.block.BlockFire"),
	VanillaStatList(new VanillaStatList(), "net.minecraft.stats.StatList"),

	VanillaAnvilChunkLoader(new VanillaAnvilChunkLoader(), "net.minecraft.world.chunk.storage.AnvilChunkLoader"),
	VanillaChunk(new VanillaChunk(), "net.minecraft.world.chunk.Chunk"),
	VanillaExtendedBlockStorage(new VanillaExtendedBlockStorage(), "net.minecraft.world.chunk.storage.ExtendedBlockStorage"),
	VanillaDataWatcher(new VanillaDataWatcher(), "net.minecraft.entity.DataWatcher"),

	VanillaNetHandlerPlayClient(new VanillaNetHandlerPlayClient(), "net.minecraft.client.network.NetHandlerPlayClient"),
	VanillaS21PacketChunkData(new VanillaS21PacketChunkData(), "net.minecraft.network.play.server.S21PacketChunkData"),
	VanillaS22PacketMultiBlockChange(new VanillaS22PacketMultiBlockChange(), "net.minecraft.network.play.server.S22PacketMultiBlockChange"),
	VanillaS26PacketMapChunkBulk(new VanillaS26PacketMapChunkBulk(), "net.minecraft.network.play.server.S26PacketMapChunkBulk"),

	FmlRegistry(new FmlRegistry(), "cpw.mods.fml.common.registry.GameData",
			"cpw.mods.fml.common.registry.FMLControlledNamespacedRegistry"),

	CofhBlockHelper(new CofhBlockHelper(), "cofh.lib.util.helpers.BlockHelper"),

	UndergroundBiomesOreUBifier(new UndergroundBiomesOreUBifier(), "exterminatorJeff.undergroundBiomes.worldGen.OreUBifier"),
	UndergroundBiomesBiomeUndergroundDecorator(new UndergroundBiomesBiomeUndergroundDecorator(), "exterminatorJeff.undergroundBiomes.worldGen.BiomeUndergroundDecorator");

	private ClassEdit(IClassNodeTransformer transformer, String... classNames) {
		this.transformer = transformer;
		this.classNames = classNames;
	}

	public static ClassEdit get(String className) {
		return editMap.get(className);
	}


	public String getName() {
		return this.name();
	}

	public IClassNodeTransformer getTransformer() {
		return transformer;
	}


	private static final Map<String, ClassEdit> editMap = new HashMap<String, ClassEdit>();

	static {
		for (ClassEdit edit : values()) {
			for (String name : edit.classNames) {
				editMap.put(name, edit);
			}
		}
	}

	private final IClassNodeTransformer transformer;
	private final String[] classNames;
}
