package ru.fewizz.idextender;

import cpw.mods.fml.relauncher.*;
import java.io.*;
import java.util.*;
import ru.fewizz.idextender.asm.*;

@IFMLLoadingPlugin.MCVersion("1.7.10")
@IFMLLoadingPlugin.TransformerExclusions({"ru.fewizz.idextender.asm"})
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

    public void injectData(final Map<String, Object> data) {
        IEConfig.init(data.get("coremodLocation"));
        IETransformer.isObfuscated = data.get("runtimeDeobfuscationEnabled");
    }

    public String getAccessTransformerClass() {
        return null;
    }
}
