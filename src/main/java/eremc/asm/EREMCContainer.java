package eremc.asm;

import java.util.Arrays;

import com.google.common.eventbus.EventBus;

import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;

public class EREMCContainer extends DummyModContainer
{
	public EREMCContainer() {
		super(new ModMetadata());

		ModMetadata meta = getMetadata();
		meta.modId = "EREMCCore";
		meta.name = "EREMCCore";
		meta.version = "@Verison@";
		meta.authorList = Arrays.asList("ryokusitai");
		meta.description = "entended registration EMC";
		meta.url = "";
        meta.credits = "";

		this.setEnabledState(true);
	}

	@Override
	public boolean registerBus(EventBus bus, LoadController controller) {
		bus.register(this);
		return true;
	}
}
