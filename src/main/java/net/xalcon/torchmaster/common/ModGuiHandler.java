package net.xalcon.torchmaster.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.xalcon.torchmaster.client.gui.GuiTerrainLighter;
import net.xalcon.torchmaster.common.container.ContainerTerrainLighter;
import net.xalcon.torchmaster.common.tiles.TileEntityTerrainLighter;

public class ModGuiHandler implements IGuiHandler
{
	public enum GuiType
	{
		TERRAIN_LIGHTER;
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if(ID == GuiType.TERRAIN_LIGHTER.ordinal()) return new ContainerTerrainLighter(player.inventory, (IInventory) world.getTileEntity(new BlockPos(x, y, z)));
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if(ID == GuiType.TERRAIN_LIGHTER.ordinal()) return new GuiTerrainLighter(player.inventory, (TileEntityTerrainLighter) world.getTileEntity(new BlockPos(x, y, z)));
		return null;
	}
}
