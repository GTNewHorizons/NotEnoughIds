package ru.fewizz.idextender;

import java.io.File;
import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;


@IFMLLoadingPlugin.MCVersion("1.7.10")
@IFMLLoadingPlugin.TransformerExclusions("ru.fewizz.idextender.asm")
public class IEPlugin implements IFMLLoadingPlugin {
	@Override
	public String[] getASMTransformerClass() {
		return new String[] { "ru.fewizz.idextender.asm.IETransformer" };
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
	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}
}
