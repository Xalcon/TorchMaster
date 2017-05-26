package net.xalcon.torchmaster.common.blocks;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.xalcon.torchmaster.TorchMasterMod;
import net.xalcon.torchmaster.common.tiles.IAutoRegisterTileEntity;
import net.xalcon.torchmaster.common.tiles.TileEntityDreadLamp;
import net.xalcon.torchmaster.common.utils.BlockUtils;

import java.util.Random;

public class BlockDreadLamp extends BlockBase implements ITileEntityProvider, IAutoRegisterTileEntity
{
	public BlockDreadLamp()
	{
		super(Material.GROUND, "dread_lamp");
		this.setHardness(1.5f);
		this.setResistance(1.0f);
		this.setLightLevel(1.0f);
	}

	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}

	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	public boolean isFullCube(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean canEntitySpawn(IBlockState state, Entity entityIn)
	{
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityDreadLamp();
	}

	@Override
	public Class<? extends TileEntity> getTileEntityClass()
	{
		return TileEntityDreadLamp.class;
	}

	@Override
	public String getTileEntityRegistryName()
	{
		return "tile_dread_lamp";
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		if (!worldIn.isRemote)
		{
			if(TorchMasterMod.ConfigHandler.isVanillaSpawnerEnabled())
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
				TorchMasterMod.Log.info("DreadLamp placed down @ "+pos+" (DIM: "+worldIn.provider.getDimension()+"); MobSpawner scan took " + diff + "ns");
			}
		}
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
	}

	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		double d0 = (double) pos.getX() + 0.5D;
		double d1 = (double) pos.getY() + 0.4D;
		double d2 = (double) pos.getZ() + 0.5D;

		worldIn.spawnParticle(EnumParticleTypes.FLAME, d0, d1 + 0.1, d2, 0.0D, 0.0D, 0.0D);
		worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + 0.1, d1, d2, 0.0D, 0.0D, 0.0D);
		worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 - 0.1, d1, d2, 0.0D, 0.0D, 0.0D);
		worldIn.spawnParticle(EnumParticleTypes.FLAME, d0, d1, d0 + 0.1, 0.0D, 0.0D, 0.0D);
		worldIn.spawnParticle(EnumParticleTypes.FLAME, d0, d1, d0 - 0.1, 0.0D, 0.0D, 0.0D);
	}
}
