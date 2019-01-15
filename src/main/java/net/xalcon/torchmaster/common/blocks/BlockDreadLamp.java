package net.xalcon.torchmaster.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Particles;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.xalcon.torchmaster.Torchmaster;
import net.xalcon.torchmaster.common.init.ModCaps;

import java.util.Random;

public class BlockDreadLamp extends Block
{
	public static final String INTERNAL_NAME = "dread_lamp";

	public BlockDreadLamp()
	{
		super(Builder.create(Material.GROUND).hardnessAndResistance(1.5f, 1f).lightValue(15));
		this.setRegistryName(Torchmaster.MODID, INTERNAL_NAME);
	}

	@OnlyIn(Dist.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean canEntitySpawn(IBlockState state, Entity entityIn)
	{
		return false;
	}

	@Override
	public void onBlockAdded(IBlockState state, World worldIn, BlockPos pos, IBlockState oldState)
	{
		super.onBlockAdded(state, worldIn, pos, oldState);

		worldIn.getCapability(ModCaps.TORCH_REGISTRY_CONTAINER)
				.ifPresent(c -> c.getDreadLampRegistry().register(pos));
	}

	@Override
	public void onReplaced(IBlockState state, World worldIn, BlockPos pos, IBlockState newState, boolean isMoving)
	{
		super.onReplaced(state, worldIn, pos, newState, isMoving);
		worldIn.getCapability(ModCaps.TORCH_REGISTRY_CONTAINER)
				.ifPresent(c -> c.getDreadLampRegistry().unregister(pos));
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void animateTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		double d0 = (double) pos.getX() + 0.5D;
		double d1 = (double) pos.getY() + 0.4D;
		double d2 = (double) pos.getZ() + 0.5D;

		worldIn.spawnParticle(Particles.FLAME, d0, d1 + 0.1, d2, 0.0D, 0.0D, 0.0D);
		worldIn.spawnParticle(Particles.FLAME, d0 + 0.1, d1, d2, 0.0D, 0.0D, 0.0D);
		worldIn.spawnParticle(Particles.FLAME, d0 - 0.1, d1, d2, 0.0D, 0.0D, 0.0D);
		worldIn.spawnParticle(Particles.FLAME, d0, d1, d0 + 0.1, 0.0D, 0.0D, 0.0D);
		worldIn.spawnParticle(Particles.FLAME, d0, d1, d0 - 0.1, 0.0D, 0.0D, 0.0D);
	}
}
