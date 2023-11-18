package ru.fewizz.idextender.asm;

import java.util.HashMap;
import java.util.Map;

import ru.fewizz.idextender.asm.transformer.CofhBlockHelper;
import ru.fewizz.idextender.asm.transformer.FmlRegistry;
import ru.fewizz.idextender.asm.transformer.MFQM;
import ru.fewizz.idextender.asm.transformer.SelfHooks;
import ru.fewizz.idextender.asm.transformer.UndergroundBiomesBiomeUndergroundDecorator;
import ru.fewizz.idextender.asm.transformer.UndergroundBiomesOreUBifier;
import ru.fewizz.idextender.asm.transformer.VanillaAnvilChunkLoader;
import ru.fewizz.idextender.asm.transformer.VanillaChunk;
import ru.fewizz.idextender.asm.transformer.VanillaDataWatcher;
import ru.fewizz.idextender.asm.transformer.VanillaItemInWorldManager;
import ru.fewizz.idextender.asm.transformer.VanillaNetHandlerPlayClient;
import ru.fewizz.idextender.asm.transformer.VanillaPlayerControllerMP;
import ru.fewizz.idextender.asm.transformer.VanillaRenderGlobal;
import ru.fewizz.idextender.asm.transformer.WorldEditBaseBlock;

public enum ClassEdit {

    SelfHooks(new SelfHooks(), new String[] { "ru.fewizz.idextender.Hooks" }),
    VanillaAnvilChunkLoader(new VanillaAnvilChunkLoader(),
            new String[] { "net.minecraft.world.chunk.storage.AnvilChunkLoader" }),
    VanillaChunk(new VanillaChunk(), new String[] { "net.minecraft.world.chunk.Chunk" }),
    VanillaDataWatcher(new VanillaDataWatcher(), new String[] { "net.minecraft.entity.DataWatcher" }),
    VanillaNetHandlerPlayClient(new VanillaNetHandlerPlayClient(),
            new String[] { "net.minecraft.client.network.NetHandlerPlayClient" }),
    VanillaRenderGlobal(new VanillaRenderGlobal(), new String[] { "net.minecraft.client.renderer.RenderGlobal" }),
    VanillaPlayerControllerMP(new VanillaPlayerControllerMP(),
            new String[] { "net.minecraft.client.multiplayer.PlayerControllerMP" }),
    VanillaItemInWorldManager(new VanillaItemInWorldManager(),
            new String[] { "net.minecraft.server.management.ItemInWorldManager" }),
    FmlRegistry(new FmlRegistry(),
            new String[] { "cpw.mods.fml.common.registry.GameData",
                    "cpw.mods.fml.common.registry.FMLControlledNamespacedRegistry" }),
    CofhBlockHelper(new CofhBlockHelper(), new String[] { "cofh.lib.util.helpers.BlockHelper" }),
    UndergroundBiomesOreUBifier(new UndergroundBiomesOreUBifier(),
            new String[] { "exterminatorJeff.undergroundBiomes.worldGen.OreUBifier" }),
    UndergroundBiomesBiomeUndergroundDecorator(new UndergroundBiomesBiomeUndergroundDecorator(),
            new String[] { "exterminatorJeff.undergroundBiomes.worldGen.BiomeUndergroundDecorator" }),
    MFQM(new MFQM(), new String[] { "MoreFunQuicksandMod.main.MFQM" }),
    WorldEditBaseBlock(new WorldEditBaseBlock(), new String[] { "com.sk89q.worldedit.blocks.BaseBlock" });

    private static final Map<String, ClassEdit> editMap;
    private final IClassNodeTransformer transformer;
    private final String[] classNames;

    ClassEdit(final IClassNodeTransformer transformer, final String[] classNames) {
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
