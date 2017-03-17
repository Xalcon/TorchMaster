package net.xalcon.torchmaster.server;

import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

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
	}

	public static TorchRegistry INSTANCE;

	static
	{
		INSTANCE = new TorchRegistry();
	}

	private List<TorchLocation> torches = new ArrayList<TorchLocation>();
	private int torchRange;
	private int torchRangeSq;

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
}
