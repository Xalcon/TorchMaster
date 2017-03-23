package net.xalcon.torchmaster.common.tiles;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.xalcon.torchmaster.TorchMasterMod;
import net.xalcon.torchmaster.common.utils.BlockUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TileEntityTerrainLighter extends TileEntity implements IInventory, ITickable
{
	private static final int FUEL_SLOT = 9;
	private int[] spiralMap;

	private List<ItemStack> stacks;
	private int burnTime;
	private int totalBurnTime;
	private int index;
	private int tick;
	private boolean done = false;

	public TileEntityTerrainLighter()
	{
		spiralMap = BlockUtils.createSpiralMap(TorchMasterMod.ConfigHandler.getTerrainLighterTorchCount());
		stacks = new ArrayList<>(this.getSizeInventory());
		for(int i = 0; i < this.getSizeInventory(); i++)
			stacks.add(ItemStack.EMPTY);
	}

	/**
	 * Returns the number of slots in the inventory.
	 */
	@Override
	public int getSizeInventory()
	{
		return 10;
	}

	@Override
	public boolean isEmpty()
	{
		for (ItemStack itemstack : this.stacks)
		{
			if (!itemstack.isEmpty())
			{
				return false;
			}
		}

		return true;
	}

	/**
	 * Returns the stack in the given slot.
	 */
	@Override
	@Nonnull
	public ItemStack getStackInSlot(int index)
	{
		return this.stacks.get(index);
	}

	/**
	 * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
	 */
	@Override
	@Nonnull
	public ItemStack decrStackSize(int index, int count)
	{
		ItemStack itemstack = ItemStackHelper.getAndSplit(this.stacks, index, count);
		this.markDirty();
		return itemstack;
	}

	/**
	 * Removes a stack from the given slot and returns it.
	 */
	@Override
	@Nonnull
	public ItemStack removeStackFromSlot(int index)
	{
		return ItemStackHelper.getAndRemove(this.stacks, index);
	}

	/**
	 * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
	 */
	@Override
	public void setInventorySlotContents(int index, @Nonnull ItemStack stack)
	{
		this.stacks.set(index, stack);

		if (stack.getCount() > this.getInventoryStackLimit())
		{
			stack.setCount(this.getInventoryStackLimit());
		}

		this.markDirty();
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);

		NBTTagList nbttaglist = compound.getTagList("Items", 10);

		for (int i = 0; i < nbttaglist.tagCount(); ++i)
		{
			NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
			int j = nbttagcompound.getByte("Slot") & 255;

			if (j >= 0 && j < this.stacks.size())
			{
				this.stacks.set(j, new ItemStack(nbttagcompound));
			}
		}

		this.index = compound.getInteger("Index");
		this.burnTime = compound.getInteger("BurnTime");
		this.totalBurnTime = compound.getInteger("TotalBurnTime");
		this.done = this.index >= this.getTorchPlacedMax();
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		super.onDataPacket(net, pkt);
		readFromNBT(pkt.getNbtCompound());
	}

	@Override
	@Nonnull
	public NBTTagCompound getUpdateTag()
	{
		return writeToNBT(super.getUpdateTag());
	}

	@Nullable
	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		return new SPacketUpdateTileEntity(this.pos, 0, this.getUpdateTag());
	}

	@Override
	@Nonnull
	public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound compound)
	{
		super.writeToNBT(compound);

		NBTTagList nbttaglist = new NBTTagList();

		for (int i = 0; i < this.stacks.size(); ++i)
		{
			ItemStack stack = this.stacks.get(i);
			if (stack != null && !stack.isEmpty())
			{
				NBTTagCompound nbttagcompound = new NBTTagCompound();
				nbttagcompound.setByte("Slot", (byte) i);
				stack.writeToNBT(nbttagcompound);
				nbttaglist.appendTag(nbttagcompound);
			}
		}

		compound.setInteger("Index", this.index);
		compound.setInteger("BurnTime", this.burnTime);
		compound.setInteger("TotalBurnTime", this.totalBurnTime);
		compound.setTag("Items", nbttaglist);

		return compound;
	}

	@Override
	@Nonnull
	public ITextComponent getDisplayName()
	{
		return new TextComponentTranslation(this.getName());
	}

	/**
	 * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended.
	 */
	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public boolean isUsableByPlayer(@Nonnull EntityPlayer player)
	{
		return false;
	}

	@Override
	public void openInventory(@Nonnull EntityPlayer player) { }

	@Override
	public void closeInventory(@Nonnull EntityPlayer player) { }

	/**
	 * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot. For
	 * guis use Slot.isItemValid
	 */
	@Override
	public boolean isItemValidForSlot(int index, @Nonnull ItemStack stack)
	{
		return index == 9 ? TileEntityFurnace.isItemFuel(stack) : isItemAllowed(stack);
	}

	@Override
	public int getField(int id)
	{
		return 0;
	}

	@Override
	public void setField(int id, int value) { }

	@Override
	public int getFieldCount()
	{
		return 0;
	}

	@Override
	public void clear()
	{
		for (int i = 0; i < this.stacks.size(); ++i)
		{
			this.stacks.set(i, ItemStack.EMPTY);
		}
	}

	@Override
	public String getName()
	{
		return "container.terrain_lighter";
	}

	@Override
	public boolean hasCustomName()
	{
		return false;
	}

	@Override
	public void update()
	{
		if (this.isBurningFuel())
			this.burnTime--;

		World world = getWorld();

		if (world.isRemote) return;
		if (done) return;
		if (tick++ % 5 != 0) return;
		if (!this.getWorld().isBlockPowered(this.pos)) return;

		boolean updated = false;
		ItemStack fuelStack = this.stacks.get(FUEL_SLOT);
		if (this.burnTime <= 0 && fuelStack != null && fuelStack.getCount() > 0)
		{
			int burnTime = TileEntityFurnace.getItemBurnTime(fuelStack);
			if (burnTime > 0)
			{
				this.burnTime = this.totalBurnTime = burnTime;
				Item fuelItem = fuelStack.getItem();
				fuelStack.shrink(1);
				if (fuelStack.isEmpty())
				{
					this.stacks.set(FUEL_SLOT, fuelItem.getContainerItem(fuelStack));
				}
				updated = true;
			}
		}

		int torchSlot = getTorchSlot();
		if (torchSlot >= 0 && this.burnTime > 0)
		{
			IBlockState torchBlockState = BlockUtils.getBlockStateFromItemStack(this.stacks.get(torchSlot));
			if (torchBlockState == null) return;
			BlockPos gridPos = getPosFromIndex(index);

			int height = world.getHeight(new BlockPos(gridPos.getX(), world.getActualHeight(), gridPos.getZ())).getY();
			int maxY = this.pos.getY() + 8;
			int minY = this.pos.getY() - 8;
			if (height > minY)
			{
				if (height > maxY) height = maxY;
				for (int y = height + 1; y > minY; y--)
				{
					BlockPos checkPos = new BlockPos(gridPos.getX(), y, gridPos.getZ());
					IBlockState blockState = world.getBlockState(checkPos);
					IBlockState upState = world.getBlockState(checkPos.up());
					if (blockState.getBlock().canPlaceTorchOnTop(blockState, world, checkPos) && upState.getMaterial().isReplaceable() && !upState.getMaterial().isLiquid())
					{
						world.setBlockState(checkPos.up(), torchBlockState);
						this.decrStackSize(torchSlot, 1);
						updated = true;
						break;
					}
				}
			}

			index++;
			if (index >= getTorchPlacedMax())
				done = true;
		}

		if (updated)
		{
			this.markDirty();
			IBlockState meState = world.getBlockState(this.pos);
			world.notifyBlockUpdate(this.pos, meState, meState, 3);
		}
	}

	private BlockPos getPosFromIndex(int index)
	{
		int spacing = TorchMasterMod.ConfigHandler.getTerrainLighterSpacing();
		int x = this.spiralMap[index * 2] * spacing + this.getPos().getX();
		int z = this.spiralMap[index * 2 + 1] * spacing + this.getPos().getZ();
		return new BlockPos(x, 0, z);
	}

	private int getTorchSlot()
	{
		for (int i = 0; i < 9; i++)
		{
			if (this.stacks.get(i) != null && this.stacks.get(i).getCount() > 0)
				return i;
		}
		return -1;
	}

	public static boolean isItemAllowed(ItemStack stack)
	{
		return TorchMasterMod.ConfigHandler.getTerrainLighterTorches().contains(stack.getItem().getRegistryName().toString());
	}

	public boolean isBurningFuel()
	{
		return this.burnTime > 0;
	}

	public int getTorchesPlaced()
	{
		return this.index;
	}

	public int getTorchPlacedMax()
	{
		int torchCount = TorchMasterMod.ConfigHandler.getTerrainLighterTorchCount() * 2 + 1;
		return torchCount * torchCount;
	}

	public int getBurnLeftScaled(int pixel)
	{
		float p = (float) this.burnTime / this.totalBurnTime;
		return (int) (pixel * p);
	}

	public int getProgressScaled(int pixel)
	{
		float p = (float)this.index / getTorchPlacedMax();
		return (int) (pixel * p);
	}
}
