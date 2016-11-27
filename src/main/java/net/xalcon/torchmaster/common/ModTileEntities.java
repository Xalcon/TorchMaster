package net.xalcon.torchmaster.common;

import net.minecraftforge.fml.common.registry.GameRegistry;
import net.xalcon.torchmaster.common.tiles.TileEntityMegaTorch;

public class ModTileEntities
{
	public static void init()
	{
		GameRegistry.registerTileEntity(TileEntityMegaTorch.class, "tile_mega_torch");
	}
}
