package net.xalcon.torchmaster.common.blocks;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.xalcon.torchmaster.common.ModBlocks;

import java.util.Random;

public class BlockDreadLamp extends BlockBase
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
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		int xS = pos.getX() - 7;
		int xE = pos.getX() + 7;
		int yS = MathHelper.clamp_int(pos.getY() - 7, 1, worldIn.getHeight() - 1);
		int yE = MathHelper.clamp_int(pos.getY() + 7, 1, worldIn.getHeight() - 1);
		int zS = pos.getZ() - 7;
		int zE = pos.getZ() + 7;

		IBlockState light = ModBlocks.InvisibleLight.getBlockState().getBaseState();

		for(int x = xS; x <= xE; x++)
		{
			for(int y = yS; y <= yE; y++)
			{
				for(int z = zS; z <= zE; z++)
				{
					BlockPos lightPos = new BlockPos(x, y, z);
					if(x == pos.getX() && y == pos.getY() && z == pos.getZ()) continue;
					if(worldIn.getBlockState(lightPos).getBlock() == Blocks.AIR)
						worldIn.setBlockState(lightPos, light);
				}
			}
		}
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
