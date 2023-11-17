package ru.fewizz.idextender.core;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.launchwrapper.Launch;

import com.gtnewhorizon.gtnhmixins.IEarlyMixinLoader;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import ru.fewizz.idextender.IEConfig;
import ru.fewizz.idextender.asm.IETransformer;
import ru.fewizz.idextender.mixins.Mixins;

@IFMLLoadingPlugin.MCVersion("1.7.10")
@IFMLLoadingPlugin.TransformerExclusions({ "ru.fewizz.idextender.asm" })
@IFMLLoadingPlugin.DependsOn("cofh.asm.LoadingPlugin")
public class IDExtenderCore implements IFMLLoadingPlugin, IEarlyMixinLoader {

    @Override
    public String getMixinConfig() {
        return "mixins.notenoughIDs.early.json";
    }

    @Override
    public List<String> getMixins(Set<String> loadedCoreMods) {
        return Mixins.getEarlyMixins(loadedCoreMods);
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[] { IETransformer.class.getName() };
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
        IEConfig.init((File) data.get("coremodLocation"));
        IETransformer.isObfuscated = !((boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment"));;
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
