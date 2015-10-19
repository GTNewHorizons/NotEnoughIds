package ru.fewizz.idextender;

import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;


@IFMLLoadingPlugin.MCVersion("1.7.10")
@IFMLLoadingPlugin.TransformerExclusions("ru.fewizz.idextender.asm")
@IFMLLoadingPlugin.SortingIndex(value = 1001) // to run after the srg mapper
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

	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}
}
