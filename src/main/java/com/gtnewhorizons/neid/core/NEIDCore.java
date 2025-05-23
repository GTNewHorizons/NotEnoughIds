package com.gtnewhorizons.neid.core;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.gtnewhorizon.gtnhlib.mixin.IMixins;
import com.gtnewhorizon.gtnhmixins.IEarlyMixinLoader;
import com.gtnewhorizons.neid.asm.NEIDTransformer;
import com.gtnewhorizons.neid.mixins.Mixins;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.MCVersion("1.7.10")
@IFMLLoadingPlugin.TransformerExclusions({ "com.gtnewhorizons.neid.asm" })
@IFMLLoadingPlugin.Name("NotEnoughIDs Core")
public class NEIDCore implements IFMLLoadingPlugin, IEarlyMixinLoader {

    private static boolean isObfuscated;

    @Override
    public String getMixinConfig() {
        return "mixins.neid.early.json";
    }

    @Override
    public List<String> getMixins(Set<String> loadedCoreMods) {
        return IMixins.getEarlyMixins(Mixins.class, loadedCoreMods);
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[] { NEIDTransformer.class.getName() };
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
        isObfuscated = (boolean) data.get("runtimeDeobfuscationEnabled");
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

    public static boolean isObfuscated() {
        return isObfuscated;
    }
}
