package net.xalcon.torchmaster.common.tiles;

import com.mojang.authlib.GameProfile;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
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
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.xalcon.torchmaster.TorchMasterMod;
import net.xalcon.torchmaster.common.utils.BlockUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TileEntityTerrainLighter extends TileEntity implements ITickable
{
	private static final GameProfile TERRAIN_LIGHTER_IDENTITY = new GameProfile(UUID.fromString("d80c982d-de38-43e6-8554-de12f86914d9"), TorchMasterMod.MODID + ":terrain_lighter");
	private static final int FUEL_SLOT = 9;
	private int[] spiralMap;

	private ItemStackHandler inventory;

	private int burnTime;
	private int totalBurnTime;
	private int index;
	private int tick;
	private boolean done = false;

	public TileEntityTerrainLighter()
	{
		this.spiralMap = BlockUtils.createSpiralMap(TorchMasterMod.ConfigHandler.getTerrainLighterTorchCount());
		this.inventory = new ItemStackHandler(10);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);

		this.inventory.deserializeNBT(compound.getCompoundTag("Items"));

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

		compound.setInteger("Index", this.index);
		compound.setInteger("BurnTime", this.burnTime);
		compound.setInteger("TotalBurnTime", this.totalBurnTime);
		compound.setTag("Items", this.inventory.serializeNBT());

		return compound;
	}

	@Override
	@Nonnull
	public ITextComponent getDisplayName()
	{
		return new TextComponentTranslation("container." + TorchMasterMod.MODID + ".terrain_lighter");
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
		if(this.burnTime <= 0)
		{
			ItemStack fuelStack = this.inventory.extractItem(FUEL_SLOT, 1, true);
			if (!fuelStack.isEmpty())
			{
				int burnTime = TileEntityFurnace.getItemBurnTime(fuelStack);
				if (burnTime > 0)
				{
					this.inventory.extractItem(FUEL_SLOT, 1, false);
					this.burnTime = this.totalBurnTime = burnTime;
					Item fuelItem = fuelStack.getItem();
					if (!fuelStack.isEmpty() && fuelItem.hasContainerItem(fuelStack))
					{
						ItemStack containerItemStack = fuelItem.getContainerItem(fuelStack);
						this.inventory.setStackInSlot(FUEL_SLOT, containerItemStack);
					}
					updated = true;
				}
			}
		}

		int torchSlot = getTorchSlot();
		if (torchSlot >= 0 && this.burnTime > 0)
		{
			//IBlockState torchBlockState = BlockUtils.getBlockStateFromItemStack(this.inventory.getStackInSlot(torchSlot));
			//if (torchBlockState.getBlock() == Blocks.AIR) return;
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
						ItemStack stack = this.inventory.extractItem(torchSlot, 1, false);
						if(!stack.isEmpty())
						{
							FakePlayer fakePlayer = FakePlayerFactory.get((WorldServer) this.world, TERRAIN_LIGHTER_IDENTITY);
							// move the player to the light position and let him face down
							// some mods use the players facing to determine placement details
							// instead of relying on the EnumFacing :( *points at BiblioCraft*
							fakePlayer.setHeldItem(EnumHand.MAIN_HAND, stack);
							fakePlayer.setPosition(checkPos.getX() + 0.5, checkPos.getY() + 1.5, checkPos.getZ() + 0.5);
							fakePlayer.rotationPitch = 90f;
							EnumActionResult result = stack.onItemUse(fakePlayer, this.world, checkPos, EnumHand.MAIN_HAND, EnumFacing.UP, 0.5f, 1f, 0.5f);
							if(result == EnumActionResult.SUCCESS)
							{
								updated = true;
							}
						}
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
			ItemStack stack = this.inventory.getStackInSlot(i);
			if (!stack.isEmpty() && isItemAllowed(stack))
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

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing)
	{
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	}

	@Nullable
	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing)
	{
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
				? CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.inventory)
				: super.getCapability(capability, facing);
	}
}
