package net.xalcon.torchmaster.common.tiles;

import net.minecraft.init.Particles;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.xalcon.torchmaster.client.ClientTorchRegistries;
import net.xalcon.torchmaster.common.init.ModBlocks;
import net.xalcon.torchmaster.common.TorchmasterConfig;
import net.xalcon.torchmaster.common.init.ModTileEntities;

public class TileEntityMegaTorch extends TileEntity
{
	public TileEntityMegaTorch()
	{
		super(ModTileEntities.MEGA_TORCH);
	}

	@Override
	public void onLoad()
	{
		super.onLoad();

		// client side torch caching
		if(world.isRemote)
			ClientTorchRegistries.getRegistryForDimension(world.getDimension().getId()).addTorch(pos);
	}

	@Override
	public void onChunkUnloaded()
	{
		super.onChunkUnloaded();

		// client side torch caching
		if(world.isRemote)
			ClientTorchRegistries.getRegistryForDimension(world.getDimension().getId()).removeTorch(pos);
	}
}
