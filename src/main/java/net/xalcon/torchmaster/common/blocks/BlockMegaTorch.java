package net.xalcon.torchmaster.common.blocks;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.xalcon.torchmaster.TorchMasterMod;
import net.xalcon.torchmaster.common.tiles.TileEntityMegaTorch;
import net.xalcon.torchmaster.common.utils.BlockUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class BlockMegaTorch extends BlockBase implements ITileEntityProvider
{
	protected static final AxisAlignedBB STANDING_AABB = new AxisAlignedBB(0.35, 0.0D, 0.35, 0.65, 1.0, 0.65);

	public BlockMegaTorch()
	{
		super(Material.WOOD, "mega_torch");
		this.setHardness(1.5f);
		this.setResistance(1.0f);
		this.setLightLevel(1.0f);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityMegaTorch();
	}

	@SuppressWarnings("deprecation")
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@SuppressWarnings("deprecation")
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}

	@SuppressWarnings("deprecation")
	@Nullable
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, @Nonnull IBlockAccess worldIn, @Nonnull BlockPos pos)
	{
		return NULL_AABB;
	}

	@SuppressWarnings("deprecation")
	@Nonnull
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return STANDING_AABB;
	}

	@SideOnly(Side.CLIENT)
	@Nonnull
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		if (!worldIn.isRemote)
		{
			if(TorchMasterMod.Configuration.isVanillaSpawnerEnabled())
			{
				long startTime = System.nanoTime();
				for (TileEntity te : worldIn.tickableTileEntities)
				{
					if (te instanceof TileEntityMobSpawner)
					{
						BlockUtils.addTagToSpawner("IsSpawnerMob", (TileEntityMobSpawner) te);
					}
				}
				long diff = System.nanoTime() - startTime;
				TorchMasterMod.Log.info("MegaTorch placed down @ "+pos+" (DIM: "+worldIn.provider.getDimension()+"); MobSpawner scan took " + diff + "ns");
			}
		}
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
	}

	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		double d0 = (double) pos.getX() + 0.5D;
		double d1 = (double) pos.getY() + 1.1D;
		double d2 = (double) pos.getZ() + 0.5D;

		worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, 0.0D, 0.0D, 0.0D);
		worldIn.spawnParticle(EnumParticleTypes.FLAME, d0, d1, d2, 0.0D, 0.0D, 0.0D);
	}
}
