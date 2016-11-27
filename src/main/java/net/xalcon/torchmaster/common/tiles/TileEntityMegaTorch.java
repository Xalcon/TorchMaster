package net.xalcon.torchmaster.common.tiles;

import net.minecraft.tileentity.TileEntity;
import net.xalcon.torchmaster.server.TorchRegistry;

public class TileEntityMegaTorch extends TileEntity
{
	@Override
	public void onLoad()
	{
		super.onLoad();
		if(!worldObj.isRemote)
			TorchRegistry.INSTANCE.registerTorch(this.worldObj, this.pos);
	}

	@Override
	public void invalidate()
	{
		super.invalidate();
		if(!worldObj.isRemote)
			TorchRegistry.INSTANCE.unregisterTorch(this.worldObj, this.pos);
	}

	@Override
	public void onChunkUnload()
	{
		super.onChunkUnload();
		if(!worldObj.isRemote)
			TorchRegistry.INSTANCE.unregisterTorch(this.worldObj, this.pos);
	}
}
