package net.xalcon.torchmaster.common.tiles;

import net.minecraft.init.Particles;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.xalcon.torchmaster.client.ClientTorchRegistries;
import net.xalcon.torchmaster.common.ModBlocks;
import net.xalcon.torchmaster.common.TorchmasterConfig;

public class TileEntityMegaTorch extends TileEntity implements ITickable
{
	private int burnValueLeft;
	private int burnRate;
	private boolean isExtinguished;

	public TileEntityMegaTorch(TileEntityType<TileEntityMegaTorch> tileEntityType)
	{
		super(tileEntityType);
		this.burnRate = TorchmasterConfig.MegaTorchBurnoutRate;
	}

	public void relightTorch(int burnValueLeft)
	{
		this.burnValueLeft = Math.max(0, burnValueLeft);
		this.isExtinguished = false;
	}

	public void readSyncNbt(NBTTagCompound compound)
	{
		this.burnValueLeft = compound.getInt("burnValueLeft");
		this.isExtinguished = compound.getBoolean("isExtinguished");
	}

	public void writeSyncNbt(NBTTagCompound compound)
	{
		compound.setInt("burnValueLeft", this.burnValueLeft);
		compound.setBoolean("isExtinguished", this.isExtinguished);
	}

	@Override
	public void read(NBTTagCompound compound)
	{
		super.read(compound);
		this.readSyncNbt(compound);
	}

	@Override
	public NBTTagCompound write(NBTTagCompound compound)
	{
		this.writeSyncNbt(compound);
		return super.write(compound);
	}

	@Override
	public void handleUpdateTag(NBTTagCompound compound)
	{
		super.handleUpdateTag(compound);
		this.readSyncNbt(compound);
	}

	@Override
	public NBTTagCompound getUpdateTag()
	{
		NBTTagCompound compound = super.getUpdateTag();
		this.writeSyncNbt(compound);
		return compound;
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
		{
			ClientTorchRegistries.getRegistryForDimension(world.getDimension().getId()).removeTorch(pos);
		}
	}

	@Override
	public void tick()
	{
		if(this.burnRate > 0 && !this.isExtinguished)
		{
			this.burnValueLeft -= Math.min(this.burnValueLeft, this.burnRate);
			if(burnValueLeft == 0)
			{
				if(!this.world.isRemote)
				{
					this.getWorld().spawnParticle(Particles.SMOKE, this.pos.getX() + 0.5, this.pos.getY() + 1, this.pos.getZ() + 0.5, 0, 1, 0);
					this.getWorld().playSound(null, this.pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1.0f, 1.0f);
					this.getWorld().setBlockState(this.getPos(), ModBlocks.getMegaTorch().getTorchState(false));
				}
				this.isExtinguished = true;
			}
		}
	}
}
