package net.xalcon.torchmaster.common;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

/**
 * Created by xalcon on 27.11.2016.
 */
public class ConfigHandler
{
	public static Configuration config;

	public static int MegaTorchRange;

	public static void init(File configFile)
	{
		if (config == null)
		{
			config = new Configuration(configFile);
			loadConfiguration();
		}
	}

	private static void loadConfiguration()
	{
		try
		{
			MegaTorchRange = config.getInt("MegaTorchRange", "general", 32, 0, 512, "The radius of the spawn prevention. Default: 32");
		}
		catch (Exception e)
		{
		}
		finally
		{
			if (config.hasChanged()) config.save();
		}
	}
}
