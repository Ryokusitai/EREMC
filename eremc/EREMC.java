package eremc;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;


@Mod(modid = EREMC.MODID, name =  EREMC.MODNAME, version =  EREMC.VERSION, dependencies="required-after:ProjectE;after:BuildCraft;after:ic2")
public class EREMC
{
	public static final String MODID = "EREMC";			// Extended Registration Emc
	public static final String MODNAME = "EREMC";
	public static final String VERSION = "@VERSION@";

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		EmcHandler.registerEMC();
	}

}
