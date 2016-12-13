package net.xalcon.torchmaster.common.tiles;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nullable;

public class TileEntityTerrainLighter extends TileEntity implements IInventory, ITickable
{
	private ItemStack[] stacks = new ItemStack[9];

	/**
	 * Returns the number of slots in the inventory.
	 */
	@Override
	public int getSizeInventory()
	{
		return 9;
	}

	/**
	 * Returns the stack in the given slot.
	 */
	@Nullable
	@Override
	public ItemStack getStackInSlot(int index)
	{
		return this.stacks[index];
	}

	/**
	 * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
	 */
	@Nullable
	@Override
	public ItemStack decrStackSize(int index, int count)
	{
		ItemStack itemstack = ItemStackHelper.getAndSplit(this.stacks, index, count);

		if (itemstack != null)
		{
			this.markDirty();
		}

		return itemstack;
	}

	/**
	 * Removes a stack from the given slot and returns it.
	 */
	@Nullable
	@Override
	public ItemStack removeStackFromSlot(int index)
	{
		return ItemStackHelper.getAndRemove(this.stacks, index);
	}

	/**
	 * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
	 */
	@Override
	public void setInventorySlotContents(int index, @Nullable ItemStack stack)
	{
		this.stacks[index] = stack;

		if (stack != null && stack.stackSize > this.getInventoryStackLimit())
		{
			stack.stackSize = this.getInventoryStackLimit();
		}

		this.markDirty();
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);

		NBTTagList nbttaglist = compound.getTagList("Items", 10);
		this.stacks = new ItemStack[this.getSizeInventory()];

		for (int i = 0; i < nbttaglist.tagCount(); ++i)
		{
			NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
			int j = nbttagcompound.getByte("Slot") & 255;

			if (j >= 0 && j < this.stacks.length)
			{
				this.stacks[j] = ItemStack.loadItemStackFromNBT(nbttagcompound);
			}
		}

		this.x = compound.getInteger("x");
		this.z = compound.getInteger("z");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);

		NBTTagList nbttaglist = new NBTTagList();

		for (int i = 0; i < this.stacks.length; ++i)
		{
			if (this.stacks[i] != null)
			{
				NBTTagCompound nbttagcompound = new NBTTagCompound();
				nbttagcompound.setByte("Slot", (byte)i);
				this.stacks[i].writeToNBT(nbttagcompound);
				nbttaglist.appendTag(nbttagcompound);
			}
		}

		compound.setInteger("x", this.x);
		compound.setInteger("z", this.z);

		compound.setTag("Items", nbttaglist);

		return compound;
	}

	@Nullable
	@Override
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

	/**
	 * Don't rename this method to canInteractWith due to conflicts with Container
	 */
	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return this.worldObj.getTileEntity(this.pos) != this ? false : player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory(EntityPlayer player) { }

	@Override
	public void closeInventory(EntityPlayer player) { }

	/**
	 * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot. For
	 * guis use Slot.isItemValid
	 */
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		return true;
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
		for (int i = 0; i < this.stacks.length; ++i)
		{
			this.stacks[i] = null;
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

	private int x;
	private int z;

	@Override
	public void onLoad()
	{
		super.onLoad();
		this.x = this.pos.getX() - 32;
		this.z = this.pos.getZ() - 32;
	}

	int tick;
	boolean done = false;

	@Override
	public void update()
	{
		if(this.getWorld().isRemote) return;
		if(done) return;
		if(tick++ % 5 != 0) return;

		int torchSlot = getTorchSlot();
		if (torchSlot >= 0)
		{
			int height = worldObj.getHeight(new BlockPos(x, worldObj.getActualHeight(), z)).getY();
			int maxY =  this.pos.getY() + 16;
			int minY = this.pos.getY() - 16;
			if(height < maxY && height > minY)
			{
				for(int y = height + 1; y > minY; y--)
				{
					BlockPos checkPos = new BlockPos(x, y, z);
					IBlockState blockState = worldObj.getBlockState(checkPos);
					IBlockState upState = worldObj.getBlockState(checkPos.up());
					if(Blocks.TORCH.canPlaceTorchOnTop(blockState, worldObj, checkPos) && upState.getMaterial().isReplaceable() && !upState.getMaterial().isLiquid())
					{
						worldObj.setBlockState(checkPos.up(), Blocks.TORCH.getDefaultState());
						break;
					}
				}
			}
			x += 5;
			if(x >= this.getPos().getX() + 32)
			{
				x = this.getPos().getX() - 32;
				z += 5;
				if(z >= this.getPos().getZ() + 32)
					done = true;
			}
		}
	}

	public int getTorchSlot()
	{
		for(int i = 0; i < this.stacks.length; i++)
		{
			if(this.stacks[i] != null && this.stacks[i].stackSize > 0)
				return i;
		}
		return -1;
	}
}
