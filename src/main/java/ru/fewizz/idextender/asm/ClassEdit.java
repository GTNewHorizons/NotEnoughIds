package ru.fewizz.idextender.asm;

import ru.fewizz.idextender.asm.transformer.*;
import java.util.*;

public enum ClassEdit
{
    SelfHooks((IClassNodeTransformer)new SelfHooks(), new String[] { "ru.fewizz.idextender.Hooks" }), 
    VanillaBlockFire((IClassNodeTransformer)new VanillaBlockFire(), new String[] { "net.minecraft.block.BlockFire" }), 
    VanillaStatList((IClassNodeTransformer)new VanillaStatList(), new String[] { "net.minecraft.stats.StatList" }), 
    VanillaAnvilChunkLoader((IClassNodeTransformer)new VanillaAnvilChunkLoader(), new String[] { "net.minecraft.world.chunk.storage.AnvilChunkLoader" }), 
    VanillaChunk((IClassNodeTransformer)new VanillaChunk(), new String[] { "net.minecraft.world.chunk.Chunk" }), 
    VanillaExtendedBlockStorage((IClassNodeTransformer)new VanillaExtendedBlockStorage(), new String[] { "net.minecraft.world.chunk.storage.ExtendedBlockStorage" }), 
    VanillaDataWatcher((IClassNodeTransformer)new VanillaDataWatcher(), new String[] { "net.minecraft.entity.DataWatcher" }), 
    VanillaNetHandlerPlayClient((IClassNodeTransformer)new VanillaNetHandlerPlayClient(), new String[] { "net.minecraft.client.network.NetHandlerPlayClient" }), 
    VanillaS21PacketChunkData((IClassNodeTransformer)new VanillaS21PacketChunkData(), new String[] { "net.minecraft.network.play.server.S21PacketChunkData" }), 
    VanillaS22PacketMultiBlockChange((IClassNodeTransformer)new VanillaS22PacketMultiBlockChange(), new String[] { "net.minecraft.network.play.server.S22PacketMultiBlockChange" }), 
    VanillaS24PacketBlockActivation((IClassNodeTransformer)new VanillaS24PacketBlockActivation(), new String[] { "net.minecraft.network.play.server.S24PacketBlockAction" }), 
    VanillaS26PacketMapChunkBulk((IClassNodeTransformer)new VanillaS26PacketMapChunkBulk(), new String[] { "net.minecraft.network.play.server.S26PacketMapChunkBulk" }), 
    VanillaRenderGlobal((IClassNodeTransformer)new VanillaRenderGlobal(), new String[] { "net.minecraft.client.renderer.RenderGlobal" }), 
    VanillaPlayerControllerMP((IClassNodeTransformer)new VanillaPlayerControllerMP(), new String[] { "net.minecraft.client.multiplayer.PlayerControllerMP" }), 
    VanillaItemInWorldManager((IClassNodeTransformer)new VanillaItemInWorldManager(), new String[] { "net.minecraft.server.management.ItemInWorldManager" }), 
    VanillaWorld((IClassNodeTransformer)new VanillaWorld(), new String[] { "net.minecraft.world.World" }), 
    FmlRegistry((IClassNodeTransformer)new FmlRegistry(), new String[] { "cpw.mods.fml.common.registry.GameData", "cpw.mods.fml.common.registry.FMLControlledNamespacedRegistry" }), 
    CofhBlockHelper((IClassNodeTransformer)new CofhBlockHelper(), new String[] { "cofh.lib.util.helpers.BlockHelper" }), 
    UndergroundBiomesOreUBifier((IClassNodeTransformer)new UndergroundBiomesOreUBifier(), new String[] { "exterminatorJeff.undergroundBiomes.worldGen.OreUBifier" }), 
    UndergroundBiomesBiomeUndergroundDecorator((IClassNodeTransformer)new UndergroundBiomesBiomeUndergroundDecorator(), new String[] { "exterminatorJeff.undergroundBiomes.worldGen.BiomeUndergroundDecorator" }), 
    MFQM((IClassNodeTransformer)new MFQM(), new String[] { "MoreFunQuicksandMod.main.MFQM" }), 
    WorldEditBaseBlock((IClassNodeTransformer)new WorldEditBaseBlock(), new String[] { "com.sk89q.worldedit.blocks.BaseBlock" });
    
    private static final Map<String, ClassEdit> editMap;
    private final IClassNodeTransformer transformer;
    private final String[] classNames;
    
    private ClassEdit(final IClassNodeTransformer transformer, final String[] classNames) {
        this.transformer = transformer;
        this.classNames = classNames;
    }
    
    public static ClassEdit get(final String className) {
        return ClassEdit.editMap.get(className);
    }
    
    public String getName() {
        return this.name();
    }
    
    public IClassNodeTransformer getTransformer() {
        return this.transformer;
    }
    
    static {
        editMap = new HashMap<String, ClassEdit>();
        for (final ClassEdit edit : values()) {
            for (final String name : edit.classNames) {
                ClassEdit.editMap.put(name, edit);
            }
        }
    }
}
