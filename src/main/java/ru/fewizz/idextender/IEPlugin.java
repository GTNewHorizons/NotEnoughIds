package ru.fewizz.idextender;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;
import java.io.File;
import java.util.Map;
import ru.fewizz.idextender.asm.IETransformer;

@MCVersion("1.7.10")
@TransformerExclusions({"ru.fewizz.idextender.asm"})
public class IEPlugin implements IFMLLoadingPlugin {
    public String[] getASMTransformerClass() {
        return new String[] {IETransformer.class.getName()};
    }

    public String getModContainerClass() {
        return null;
    }

    public String getSetupClass() {
        return null;
    }

    public void injectData(Map<String, Object> data) {
        IEConfig.init((File) data.get("coremodLocation"));
        IETransformer.isObfuscated = (Boolean) data.get("runtimeDeobfuscationEnabled");
    }

    public String getAccessTransformerClass() {
        return null;
    }
}
