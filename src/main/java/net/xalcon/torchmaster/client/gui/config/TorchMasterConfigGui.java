package net.xalcon.torchmaster.client.gui.config;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.xalcon.torchmaster.TorchMasterMod;

public class TorchMasterConfigGui extends GuiConfig
{
	public TorchMasterConfigGui(GuiScreen parent)
	{
		super(parent, new ConfigElement(TorchMasterMod.ConfigHandler.getConfiguration().getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(),
				TorchMasterMod.MODID, false, false, GuiConfig.getAbridgedConfigPath(TorchMasterMod.ConfigHandler.getConfiguration().toString()));
	}
}
