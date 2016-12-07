package net.xalcon.torchmaster.common.tiles;

import net.minecraft.tileentity.TileEntity;
import net.xalcon.torchmaster.server.TorchRegistry;

public class TileEntityMegaTorch extends TileEntity
{
	@Override
	public void onLoad()
	{
		super.onLoad();
		if(!this.world.isRemote)
			TorchRegistry.INSTANCE.registerTorch(this.world, this.pos);
	}

	@Override
	public void invalidate()
	{
		super.invalidate();
		if(!world.isRemote)
			TorchRegistry.INSTANCE.unregisterTorch(this.world, this.pos);
	}

	@Override
	public void onChunkUnload()
	{
		super.onChunkUnload();
		if(!world.isRemote)
			TorchRegistry.INSTANCE.unregisterTorch(this.world, this.pos);
	}
}
