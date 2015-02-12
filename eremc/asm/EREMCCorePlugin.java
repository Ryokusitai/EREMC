package eremc.asm;

import java.util.Map;
import java.util.logging.Logger;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

public class EREMCCorePlugin implements IFMLLoadingPlugin
{
	public static Logger logger = Logger.getLogger("EREMCCore");

	@Override
	public String[] getASMTransformerClass() {
		return new String[]{"eremc.asm.EMCMapperTransformer"};
	}

	@Override
	public String getModContainerClass() {
		return "eremc.asm.EREMCContainer";
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
