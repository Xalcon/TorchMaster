package net.xalcon.torchmaster.common;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.xalcon.torchmaster.TorchMasterMod;

@Config(modid = TorchMasterMod.MODID)
@Mod.EventBusSubscriber(modid = TorchMasterMod.MODID)
public class TorchmasterConfig
{
    @Config.RangeInt(min = 0, max = 512)
    @Config.Comment("The radius of the spawn prevention. (Monster)")
    public static int MegaTorchRange = 64;

    @Config.Comment("The lighter item to light an unlit torch, see https://github.com/Xalcon/TorchMaster/wiki/Torch-Burnout for more info.")
    public static String[] MegaTorchLighterItems = { "minecraft:flint_and_steel" };

    @Config.RangeInt(min = 0)
    @Config.Comment("The speed at which the torch will extinguish. Set to 0 to disable.")
    public static int MegaTorchBurnoutRate = 0;

    @Config.RangeInt(min = 0)
    @Config.Comment("The burnout value. This value is removed by the amount of burnout rate per tick")
    public static int MegaTorchBurnoutValue = 1;

    @Config.Comment("If set to true, the mega torch will drop as an unlit torch when harvesting it")
    public static boolean MegaTorchExtinguishOnHarvest = false;

    @Config.RangeInt(min = 0, max = 512)
    @Config.Comment("Allows to get the lit torch when harvested with silk touch. Has no effect if MegaTorchExtinguishOnHarvest is false)")
    public static boolean MegaTorchAllowSilkTouch = true;

    @Config.RangeInt(min = 0, max = 512)
    @Config.Comment("Controls if the mega torch should allow mob spawning from vanilla spawners")
    public static boolean MegaTorchAllowVanillaSpawners = true;


    @Config.RangeInt(min = 0, max = 512)
    @Config.Comment("The radius of the spawn prevention. (Passive Animals)")
    public static int DreadLampRange = 64;


    @Config.RangeInt(min = 0)
    @Config.Comment("The amount of torches to place in each direction. The effective range is multiplied by the torch spacing (32 * 5 = 160 blocks, default 7 * 5 = 35 blocks)")
    public static int TerrainLighterTorchCount = 7;

    @Config.RangeInt(min = 0)
    @Config.Comment("The spacing between each torch. Distance of 5 means there will be a torch every 5 blocks with 4 blocks space in between.")
    public static int TerrainLighterSpacing = 5;

    @Config.RangeInt(min = 0, max = 512)
    @Config.Comment("This controls which torches are supported by the terrain lighter")
    public static String[] TerrainLighterTorches = { "minecraft:torch", "tconstruct:stone_torch" };


    @Config.Comment("Adds additional information about torchmaster items into the tooltip")
    public static boolean BeginnerTooltips = true;

    @Config.Comment("If this setting is enabled, the mega torch will block all natural spawn attempts from Lycanites Mobs in its radius")
    @Config.RequiresMcRestart
    public static boolean LycanitesMobsBlockAll = true;

    @Config.Comment("If this setting is enabled, the mega torch will block all natural spawn attempts from MoCreatures in its radius")
    @Config.RequiresMcRestart
    public static boolean MoCreaturesBlockAll = true;


    @Config.RangeInt(min = 0)
    @Config.Comment("The radius in which the feral flare should try to place lights")
    public static int feralFlareRadius = 16;

    @Config.RangeInt(min = 1)
    @Config.Comment("Controls how often the flare should try to place lights. 1 means every tick, 10 every 10th tick, etc")
    public static int feralFlareTickRate = 5;

    @Config.RangeInt(min = 1, max = 15)
    @Config.Comment("The target minimum light level to place lights for")
    public static int feralFlareMinLightLevel = 10;

    @Config.Comment("If false, lights decay slowly after the lantern has been removed. If true, the lights will be removed instantly")
    public static boolean feralFlareLightDecayInstantly = false;


    @Config.RangeInt(min = 0)
    @Config.Comment("Durability of the frozen pearl. Each removed light will remove one charge from the pearl. Set to 0 to disable durability")
    @Config.RequiresMcRestart
    public static int frozenPearlDurability = 1024;

    @SubscribeEvent
    public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
    {
        if (event.getModID().equals(TorchMasterMod.MODID))
        {
            ConfigManager.sync(TorchMasterMod.MODID, Config.Type.INSTANCE);
        }
    }
}
