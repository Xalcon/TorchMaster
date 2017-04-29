package net.xalcon.torchmaster.common;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.xalcon.torchmaster.TorchMasterMod;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;

public class ConfigHandler
{
	private Configuration config;

	private boolean megaTorchAllowVanillaSpawners;
	private int megaTorchRange;
	private int dreadLampRange;
	private int terrainLighterTorchCount;
	private int terrainLighterSpacing;
	private HashSet<String> terrainLighterTorches;
	private HashSet<String> megaTorchLighterItems;

	public ConfigHandler(File configFile)
	{
		config = new Configuration(configFile);
		loadConfigs(false);
	}

	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs)
	{
		if(eventArgs.getModID().equals(TorchMasterMod.MODID))
			loadConfigs(true);
	}

	private void loadConfigs(boolean reload)
	{
		try
		{
			megaTorchRange = config.getInt("MegaTorchRange", "general", 32, 0, 512, "The radius of the spawn prevention. (Monster)");
			this.megaTorchLighterItems = new HashSet<>(Arrays.asList(config.get("general", "MegaTorchLighterItems", new String[]{"minecraft:flint_and_steel"}, "The lighter item to light an unlit torch, see ADD_WEBSITE_HERE for more info.").getStringList()));


			dreadLampRange = config.getInt("DreadLampRange", "general", 64, 0, 512, "The radius of the spawn prevention. (Passive Animals)");
			megaTorchAllowVanillaSpawners = config.getBoolean("MegaTorchAllowVanillaSpawners", "general", true, "Allows vanilla spawners to spawn monsters inside the working radius of a mega torch");

			terrainLighterTorchCount = config.getInt("TerrainLighterTorchCount", "general", 7, 0, 32, "The amount of torches to place in each direction. The effective range is multiplied by the torch spacing (32 * 5 = 160 blocks, default 7 * 5 = 35 blocks)");
			terrainLighterSpacing = config.getInt("TerrainLighterSpacing", "general", 5, 1, 16, "The spacing between each torch. Distance of 5 means there will be a torch every 5 blocks with 4 blocks space in between.");
			terrainLighterTorches = new HashSet<>(Arrays.asList(config.get("general", "TerrainLighterTorches", new String[]{"minecraft:torch", "tconstruct:stone_torch"}, "This controls which torches are supported by the terrain lighter").getStringList()));


		}
		catch (Exception e)
		{
			TorchMasterMod.Log.error("Error loading config", e);
		}
		finally
		{
			if (config.hasChanged()) config.save();
			TorchRegistry.getMegaTorchRegistry().setTorchRange(getMegaTorchRange());
			TorchRegistry.getDreadLampRegistry().setTorchRange(getDreadLampRange());
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

	public int getDreadLampRange()
	{
		return dreadLampRange;
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

	public Configuration getConfiguration() { return this.config; }

	public HashSet<String> getMegaTorchLighterItems() { return megaTorchLighterItems; }
}
