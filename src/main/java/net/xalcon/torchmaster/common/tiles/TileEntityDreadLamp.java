package net.xalcon.torchmaster.common.tiles;

import net.minecraft.tileentity.TileEntity;
import net.xalcon.torchmaster.common.TorchRegistry;

public class TileEntityDreadLamp extends TileEntity
{
	@Override
	public void onLoad()
	{
		super.onLoad();
		if(!this.getWorld().isRemote)
			TorchRegistry.getDreadLampRegistry().registerTorch(this.getWorld(), this.getPos());
	}

	@Override
	public void invalidate()
	{
		super.invalidate();
		if(!this.getWorld().isRemote)
			TorchRegistry.getDreadLampRegistry().unregisterTorch(this.getWorld(), this.getPos());
	}

	@Override
	public void onChunkUnload()
	{
		super.onChunkUnload();
		if(!this.getWorld().isRemote)
			TorchRegistry.getDreadLampRegistry().unregisterTorch(this.getWorld(), this.getPos());
	}
}
