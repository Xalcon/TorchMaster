package net.xalcon.torchmaster.common;

import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashSet;

public class ConfigHandler
{
	public static Configuration config;

	public static int MegaTorchRange;
	public static int TerrainLighterTorchCount;
	public static int TerrainLighterSpacing;
	public static HashSet<String> TerrainLighterTorches;

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
			MegaTorchRange = config.getInt("MegaTorchRange", "general", 32, 0, 512, "The radius of the spawn prevention.");
			TerrainLighterTorchCount = config.getInt("TerrainLighterTorchCount", "general", 7, 0, 32, "The amount of torches to place in each direction. The effective range is multiplied by the torch spacing (32 * 5 = 160 blocks, default 7 * 5 = 35 blocks)");
			TerrainLighterSpacing = config.getInt("TerrainLighterSpacing", "general", 5, 1, 16, "The spacing between each torch. Distance of 5 means there will be a torch every 5 blocks with 4 blocks space in between.");
			TerrainLighterTorches = new HashSet<String>(Arrays.asList(config.get("general", "TerrainLighterTorches", new String[]{ "minecraft:torch", "tconstruct:stone_torch" }, "This controls which torches are supported by the terrain lighter").getStringList()));
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
