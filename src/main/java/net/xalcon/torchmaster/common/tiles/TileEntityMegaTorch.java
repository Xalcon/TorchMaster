package net.xalcon.torchmaster.common.tiles;

import net.minecraft.tileentity.TileEntity;
import net.xalcon.torchmaster.TorchMasterMod;
import net.xalcon.torchmaster.common.TorchRegistry;

public class TileEntityMegaTorch extends TileEntity
{
	@Override
	public void onLoad()
	{
		TorchMasterMod.Log.info("onLoad()");
		super.onLoad();
		if(!this.getWorld().isRemote)
			TorchRegistry.getMegaTorchRegistry().registerTorch(this.getWorld(), this.getPos());
	}

	@Override
	public void invalidate()
	{
		TorchMasterMod.Log.info("invalidate()");
		super.invalidate();
		if(!this.getWorld().isRemote)
			TorchRegistry.getMegaTorchRegistry().unregisterTorch(this.getWorld(), this.getPos());
	}

	@Override
	public void onChunkUnload()
	{
		TorchMasterMod.Log.info("onChunkUnload()");
		super.onChunkUnload();
		if(!this.getWorld().isRemote)
			TorchRegistry.getMegaTorchRegistry().unregisterTorch(this.getWorld(), this.getPos());
	}
}
