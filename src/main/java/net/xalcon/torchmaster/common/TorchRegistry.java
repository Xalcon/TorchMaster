package net.xalcon.torchmaster.common;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.xalcon.torchmaster.TorchMasterMod;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class TorchRegistry
{
	private class TorchLocation
	{
		public int DimensionId;
		public BlockPos Position;

		public TorchLocation(int dimensionId, BlockPos position)
		{
			DimensionId = dimensionId;
			Position = position;
		}

		public TorchLocation(NBTTagCompound entry)
		{
			this.DimensionId = entry.getInteger("d");
			this.Position = new BlockPos(entry.getInteger("x"), entry.getInteger("y"), entry.getInteger("z"));
		}

		@Override
		public boolean equals(Object o)
		{
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			TorchLocation that = (TorchLocation) o;

			if (DimensionId != that.DimensionId) return false;
			return Position != null ? Position.equals(that.Position) : that.Position == null;
		}

		@Override
		public int hashCode()
		{
			int result = DimensionId;
			result = 31 * result + (Position != null ? Position.hashCode() : 0);
			return result;
		}

		public NBTTagCompound toNbt()
		{
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setInteger("d", this.DimensionId);
			nbt.setInteger("x", this.Position.getX());
			nbt.setInteger("y", this.Position.getY());
			nbt.setInteger("z", this.Position.getZ());
			return nbt;
		}

		@Override
		public String toString()
		{
			return String.format("DIM: %d - %s", this.DimensionId, this.Position);
		}
	}

	private static TorchRegistry megaTorchRegistry;
	private static TorchRegistry dreadLampRegistry;

	public static TorchRegistry getMegaTorchRegistry() { return megaTorchRegistry; }
	public static TorchRegistry getDreadLampRegistry() { return dreadLampRegistry; }

	static
	{
		megaTorchRegistry = new TorchRegistry("mega_torch", s -> s.getBlock() == ModBlocks.getMegaTorch());
		dreadLampRegistry = new TorchRegistry("dread_lamp", s -> s.getBlock() == ModBlocks.getDreadLamp());
	}

	public TorchRegistry(String name, Predicate<IBlockState> blockValidator)
	{
		this.name = name;
		this.blockValidator = blockValidator;
	}

	private final String name;
	private final List<TorchLocation> torches = new ArrayList<TorchLocation>();
	private int torchRange;
	private int torchRangeSq;
	private Predicate<IBlockState> blockValidator;

	public void setTorchRange(int torchRange)
	{
		this.torchRange = torchRange;
		this.torchRangeSq = torchRange * torchRange;
	}

	public void registerTorch(World world, BlockPos pos)
	{
		TorchLocation torchLoc = new TorchLocation(world.provider.getDimension(), pos);
		if(!torches.contains(torchLoc))
			torches.add(torchLoc);
	}

	public void unregisterTorch(World world, BlockPos pos)
	{
		torches.remove(new TorchLocation(world.provider.getDimension(), pos));
	}

	public boolean isInRangeOfTorch(World world, BlockPos pos)
	{
		int dim = world.provider.getDimension();
		for(TorchLocation torch : torches)
		{
			if(torch.DimensionId != dim) continue;
			double dx = torch.Position.getX() + 0.5 - pos.getX();
			double dy = Math.abs(torch.Position.getY() + 0.5 - pos.getY());
			double dz = torch.Position.getZ() + 0.5 - pos.getZ();
			if((dx * dx + dz * dz) <= (this.torchRangeSq) && dy <= this.torchRange)
				return true;
		}
		return false;
	}

	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load event)
	{
		if(event.getWorld().provider.getDimension() != 0) return;
		File file = new File(DimensionManager.getCurrentSaveRootDirectory(), "data/torchmaster_"+this.name+"_reg.dat");
		if(!file.exists())
			return;
		NBTTagCompound nbt;

		try
		{
			nbt = CompressedStreamTools.read(file);
			if(nbt == null) return;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return;
		}

		NBTTagList list = nbt.getTagList("list", Constants.NBT.TAG_COMPOUND);
		for(int i = 0; i < list.tagCount(); i++)
		{
			NBTTagCompound entry = list.getCompoundTagAt(i);
			TorchLocation loc = new TorchLocation(entry);
			World world = DimensionManager.getWorld(loc.DimensionId);
			this.registerTorch(world, loc.Position);
		}
		TorchMasterMod.Log.debug("Loaded entries for " + this.name);
	}

	@SubscribeEvent
	public void onWorldSave(WorldEvent.Save event)
	{
		if(event.getWorld().provider.getDimension() != 0) return;
		File file = new File(DimensionManager.getCurrentSaveRootDirectory(), "data/torchmaster_"+this.name+"_reg.dat");

		NBTTagList tagList = new NBTTagList();
		for(TorchLocation loc : this.torches)
			tagList.appendTag(loc.toNbt());
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setTag("list", tagList);

		try
		{
			CompressedStreamTools.write(nbt, file);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private int checkIndex = 0;

	@SubscribeEvent
	public void onGlobalTick(TickEvent.ServerTickEvent event)
	{
		if(event.side == Side.CLIENT) return;
		if(this.torches.size() == 0) return;

		this.checkIndex = (this.checkIndex + 1) % this.torches.size();
		TorchLocation loc = this.torches.get(this.checkIndex);
		World world = DimensionManager.getWorld(loc.DimensionId);
		if(world == null) return;
		if(world.isBlockLoaded(loc.Position))
		{
			if(!this.blockValidator.test(world.getBlockState(loc.Position)))
			{
				TorchMasterMod.Log.info("Torch @ " + loc + " is no longer valid, removing from registry");
				unregisterTorch(world, loc.Position);
			}
		}
	}
}
