package net.xalcon.torchmaster.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.xalcon.torchmaster.client.gui.GuiFeralLantern;
import net.xalcon.torchmaster.client.gui.GuiTerrainLighter;
import net.xalcon.torchmaster.common.container.ContainerTerrainLighter;
import net.xalcon.torchmaster.common.tiles.TileEntityFeralFlareLantern;
import net.xalcon.torchmaster.common.tiles.TileEntityTerrainLighter;

public class ModGuiHandler implements IGuiHandler
{
	public enum GuiType
	{
		INVALID, TERRAIN_LIGHTER, FERAL_LANTERN;

		public static GuiType fromId(int id)
		{
			if(id < 0 || id > GuiType.values().length)
				return INVALID;
			return GuiType.values()[id];
		}
	}

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		switch (GuiType.fromId(id))
		{
			case TERRAIN_LIGHTER:
				return new ContainerTerrainLighter(player.inventory, (TileEntityTerrainLighter) world.getTileEntity(new BlockPos(x, y, z)));
			case FERAL_LANTERN:
				// feral lanterns have no server side element
			default:
				return null;
		}
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{

		switch (GuiType.fromId(id))
		{
			case TERRAIN_LIGHTER:
				return new GuiTerrainLighter(player.inventory, (TileEntityTerrainLighter) world.getTileEntity(new BlockPos(x, y, z)));
			case FERAL_LANTERN:
				return new GuiFeralLantern((TileEntityFeralFlareLantern) world.getTileEntity(new BlockPos(x, y, z)));
			default:
				return null;
		}
	}
}
