package ru.fewizz.idextender;

import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

/**
 * This class(and package) needs to be here and named exactly as it is in order for FastCraft to work with NEID.
 * FastCraft has some kind of detection for this class and operates accordingly if it is present or not. All NEID code
 * lives under the com.gtnewhorizons.neid package now. This is simply here to trick FastCraft.
 *
 * @author Cleptomania
 */
@IFMLLoadingPlugin.MCVersion("1.7.10")
public class IEPlugin implements IFMLLoadingPlugin {

    @Override
    public String[] getASMTransformerClass() {
        return new String[] {};
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

    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
