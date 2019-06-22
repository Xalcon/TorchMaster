package net.xalcon.torchmaster.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.xalcon.torchmaster.Torchmaster;
import net.xalcon.torchmaster.common.tiles.TileEntityTerrainLighter;

import javax.annotation.Nullable;

public class BlockTerrainLighter extends Block implements ITileEntityProvider
{
	public static final String INTERNAL_NAME = "terrain_lighter";

	public BlockTerrainLighter()
	{
		super(Builder.create(Material.ROCK).hardnessAndResistance(1f, 3f));
		this.setRegistryName(Torchmaster.MODID, INTERNAL_NAME);
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(IBlockState state, IBlockReader world)
	{
		return new TileEntityTerrainLighter();
	}

	@Nullable
	@Override
	public TileEntity createNewTileEntity(IBlockReader iBlockReader)
	{
		return new TileEntityTerrainLighter();
	}

	@Override
	public boolean onBlockActivated(IBlockState p_196250_1_, World p_196250_2_, BlockPos p_196250_3_, EntityPlayer p_196250_4_, EnumHand p_196250_5_, EnumFacing p_196250_6_, float p_196250_7_, float p_196250_8_, float p_196250_9_)
	{
		if(!p_196250_2_.isRemote)
		{
			// TODO
			//playerIn.openGui(TorchMasterMod.instance, ModGuiHandler.GuiType.TERRAIN_LIGHTER.ordinal(), worldIn, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}

	@Override
	public void onReplaced(IBlockState state, World worldIn, BlockPos pos, IBlockState newState, boolean isMoving)
	{
		if (state.getBlock() != newState.getBlock()) {
			TileEntity tileentity = worldIn.getTileEntity(pos);
			if (tileentity instanceof TileEntityTerrainLighter)
			{
				tileentity
						.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
						.ifPresent(itemHandler ->
						{
							for(int i = 0; i < itemHandler.getSlots(); i++)
							InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), itemHandler.getStackInSlot(i));
						});
			}

			super.onReplaced(state, worldIn, pos, newState, isMoving);
		}
	}
}
