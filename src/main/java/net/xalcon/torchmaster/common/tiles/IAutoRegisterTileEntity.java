package net.xalcon.torchmaster.common.tiles;

import net.minecraft.tileentity.TileEntity;

public interface IAutoRegisterTileEntity
{
	TileEntity createTileEntity();

	String getTileEntityRegistryName();
}
