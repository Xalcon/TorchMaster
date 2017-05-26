package net.xalcon.torchmaster.common;

import net.xalcon.torchmaster.TorchMasterMod;
import net.xalcon.torchmaster.common.blocks.*;

public class ModBlocks
{
	public static BlockMegaTorch MegaTorch;
	public static BlockTerrainLighter TerrainLighter;
	public static BlockDreadLamp DreadLamp;

	public static void init()
	{
		MegaTorch = TorchMasterMod.Proxy.registerBlock(new BlockMegaTorch());
		TerrainLighter = TorchMasterMod.Proxy.registerBlock(new BlockTerrainLighter());
		DreadLamp = TorchMasterMod.Proxy.registerBlock(new BlockDreadLamp());
	}
}
