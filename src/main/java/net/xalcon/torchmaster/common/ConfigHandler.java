package net.xalcon.torchmaster.common;

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
	private boolean megaTorchExtinguishOnHarvest;
	private boolean megaTorchAllowSilkTouch;
	private boolean beginnerTooltips;
	private int megaTorchBurnoutRate;
	private int megaTorchBurnoutValue;
	private int megaTorchRange;
	private int dreadLampRange;
	private int terrainLighterTorchCount;
	private int terrainLighterSpacing;
	private HashSet<String> terrainLighterTorches;
	private HashSet<String> megaTorchLighterItems;

	public boolean isLycaniteCompatEnabled;
	public boolean isMoCreaturesCompatEnabled;

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
			this.megaTorchRange = config.getInt("MegaTorchRange", "general", 64, 0, 512, "The radius of the spawn prevention. (Monster)");
			this.megaTorchLighterItems = new HashSet<>(Arrays.asList(config.get("general", "MegaTorchLighterItems", new String[]{"minecraft:flint_and_steel"}, "The lighter item to light an unlit torch, see ADD_WEBSITE_HERE for more info.").getStringList()));
			this.megaTorchBurnoutRate = config.getInt("MegaTorchBurnoutRate", "general", 0, 0, Integer.MAX_VALUE, "The speed at which the torch will extinguish. Set to 0 to disable.");
			this.megaTorchBurnoutValue = config.getInt("MegaTorchBurnoutValue", "general", Integer.MAX_VALUE, 0, Integer.MAX_VALUE, "The burnout value. This value is removed by the amount of burnout rate per tick");
			this.megaTorchExtinguishOnHarvest = config.getBoolean("MegaTorchExtinguishOnHarvest", "general", false, "If set to true, the mega torch will drop as an unlit torch when harvesting it");
			this.megaTorchAllowSilkTouch = config.getBoolean("MegaTorchAllowSilkTouch", "general", false, "Allows to get the lit torch when harvested with silk touch. Has no effect if MegaTorchExtinguishOnHarvest is false");

			this.dreadLampRange = config.getInt("DreadLampRange", "general", 64, 0, 512, "The radius of the spawn prevention. (Passive Animals)");
			this.megaTorchAllowVanillaSpawners = config.getBoolean("MegaTorchAllowVanillaSpawners", "general", true, "Allows vanilla spawners to spawn monsters inside the working radius of a mega torch");

			this.terrainLighterTorchCount = config.getInt("TerrainLighterTorchCount", "general", 7, 0, 32, "The amount of torches to place in each direction. The effective range is multiplied by the torch spacing (32 * 5 = 160 blocks, default 7 * 5 = 35 blocks)");
			this.terrainLighterSpacing = config.getInt("TerrainLighterSpacing", "general", 5, 1, 16, "The spacing between each torch. Distance of 5 means there will be a torch every 5 blocks with 4 blocks space in between.");
			this.terrainLighterTorches = new HashSet<>(Arrays.asList(config.get("general", "TerrainLighterTorches", new String[]{"minecraft:torch", "tconstruct:stone_torch"}, "This controls which torches are supported by the terrain lighter").getStringList()));

			this.beginnerTooltips = config.getBoolean("BeginnerTooltips", "general", true, "Adds additional information about torchmaster items into the tooltip");

			isLycaniteCompatEnabled = config.getBoolean("LycanitesMobsBlockAll", "compat", true, "If this setting is enabled, the mega torch will block all natural spawn attempts from Lycanites Mobs in its radius");
			isMoCreaturesCompatEnabled = config.getBoolean("MoCreaturesBlockAll", "compat", true, "If this setting is enabled, the mega torch will block all natural spawn attempts from MoCreatures in its radius");
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

	public boolean isMegaTorchExtinguishOnHarvest() { return megaTorchExtinguishOnHarvest; }

	public boolean isMegaTorchAllowSilkTouch() { return megaTorchAllowSilkTouch; }

	public int getMegaTorchBurnoutRate() { return megaTorchBurnoutRate; }
	public int getMegaTorchBurnoutValue() { return megaTorchBurnoutValue; }

	public boolean showBeginnerTooltips()
	{
		return beginnerTooltips;
	}
}
