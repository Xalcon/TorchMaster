package net.xalcon.torchmaster.common;

import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;

public class ConfigHandler
{
	private Configuration config;

	private boolean megaTorchAllowVanillaSpawners;
	private int megaTorchRange;
	private int terrainLighterTorchCount;
	private int terrainLighterSpacing;
	private HashSet<String> terrainLighterTorches;

	public ConfigHandler(File configFile)
	{
		config = new Configuration(configFile);
		try
		{
			megaTorchRange = config.getInt("MegaTorchRange", "general", 32, 0, 512, "The radius of the spawn prevention.");
			megaTorchAllowVanillaSpawners = config.getBoolean("MegaTorchAllowVanillaSpawners", "general", true, "Allows vanilla spawners to spawn monsters inside the working radius of a mega torch");
			terrainLighterTorchCount = config.getInt("TerrainLighterTorchCount", "general", 7, 0, 32, "The amount of torches to place in each direction. The effective range is multiplied by the torch spacing (32 * 5 = 160 blocks, default 7 * 5 = 35 blocks)");
			terrainLighterSpacing = config.getInt("TerrainLighterSpacing", "general", 5, 1, 16, "The spacing between each torch. Distance of 5 means there will be a torch every 5 blocks with 4 blocks space in between.");
			terrainLighterTorches = new HashSet<>(Arrays.asList(config.get("general", "TerrainLighterTorches", new String[]{"minecraft:torch", "tconstruct:stone_torch"}, "This controls which torches are supported by the terrain lighter").getStringList()));
		}
		catch (Exception e)
		{
		}
		finally
		{
			if (config.hasChanged()) config.save();
		}
	}

	public boolean isVanillaSpawnerEnabled()
	{
		return megaTorchAllowVanillaSpawners;
	}

	public int getMegaTorchRange()
	{
		return megaTorchRange;
	}

	public int getTerrainLighterTorchCount()
	{
		return terrainLighterTorchCount;
	}

	public int getTerrainLighterSpacing()
	{
		return terrainLighterSpacing;
	}

	public HashSet<String> getTerrainLighterTorches()
	{
		return terrainLighterTorches;
	}
}
