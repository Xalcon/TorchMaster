package net.xalcon.torchmaster.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Particles;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.xalcon.torchmaster.Torchmaster;
import net.xalcon.torchmaster.client.renderer.TorchVolumeRenderHandler;
import net.xalcon.torchmaster.common.init.ModCaps;
import net.xalcon.torchmaster.common.tiles.TileEntityMegaTorch;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class BlockMegaTorch extends Block implements ITileEntityProvider
{
	protected static final AxisAlignedBB STANDING_AABB = new AxisAlignedBB(0.35, 0.0D, 0.35, 0.65, 1.0, 0.65);

	public static final String INTERNAL_NAME = "mega_torch";

    public BlockMegaTorch()
	{
		super(Builder.create(Material.WOOD).lightValue(1).hardnessAndResistance(1.5f, 1.0f));
		this.setRegistryName(Torchmaster.MODID, INTERNAL_NAME);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}

	@Nonnull
	@Override
	public BlockRenderLayer getRenderLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public void onBlockAdded(IBlockState state, World world, BlockPos pos, IBlockState oldState)
	{
		super.onBlockAdded(state, world, pos, oldState);
		world.getCapability(ModCaps.TORCH_REGISTRY_CONTAINER)
				.ifPresent(container -> container.getMegaTorchRegistry().register(pos));
	}

	@Override
	public void onReplaced(IBlockState state, World world, BlockPos pos, IBlockState newState, boolean isMoving)
	{
		world.getCapability(ModCaps.TORCH_REGISTRY_CONTAINER)
				.ifPresent(container -> container.getMegaTorchRegistry().unregister(pos));
		super.onReplaced(state, world, pos, newState, isMoving);
	}

	@Override
	public boolean onBlockActivated(IBlockState state, World worldIn, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if(worldIn.isRemote)
		{
			TorchVolumeRenderHandler.DyeColor color = TorchVolumeRenderHandler.DyeColor.FromItemStack(player.getHeldItem(hand));
			if(color != null)
				TorchVolumeRenderHandler.toggle(pos, color);
			else if(player.getHeldItem(hand).isEmpty())
				TorchVolumeRenderHandler.remove(pos);
		}
		return true;
	}

	@Nullable
	@Override
	public TileEntity createNewTileEntity(IBlockReader iBlockReader)
	{
		return new TileEntityMegaTorch();
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(IBlockState state, IBlockReader world)
	{
		return new TileEntityMegaTorch();
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void animateTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		double d0 = (double) pos.getX() + 0.5D;
		double d1 = (double) pos.getY() + 1.1D;
		double d2 = (double) pos.getZ() + 0.5D;

		worldIn.spawnParticle(Particles.SMOKE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
		worldIn.spawnParticle(Particles.FLAME, d0, d1, d2, 0.0D, 0.0D, 0.0D);
	}
}
