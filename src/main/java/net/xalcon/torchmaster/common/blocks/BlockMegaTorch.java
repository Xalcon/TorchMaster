package net.xalcon.torchmaster.common.blocks;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.xalcon.torchmaster.TorchMasterMod;
import net.xalcon.torchmaster.client.renderer.TorchVolumeRenderHandler;
import net.xalcon.torchmaster.common.ModCaps;
import net.xalcon.torchmaster.common.TorchmasterConfig;
import net.xalcon.torchmaster.common.items.ItemBlockMegaTorch;
import net.xalcon.torchmaster.common.logic.ITorchRegistryContainer;
import net.xalcon.torchmaster.common.tiles.IAutoRegisterTileEntity;
import net.xalcon.torchmaster.common.tiles.TileEntityMegaTorch;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Random;

public class BlockMegaTorch extends BlockBase implements ITileEntityProvider, IAutoRegisterTileEntity
{
	protected static final AxisAlignedBB STANDING_AABB = new AxisAlignedBB(0.35, 0.0D, 0.35, 0.65, 1.0, 0.65);

	public static final PropertyBool BURNING = PropertyBool.create("burning");
	public static final String INTERNAL_NAME = "mega_torch";

    public BlockMegaTorch()
	{
		super(Material.WOOD, INTERNAL_NAME);
		this.setHardness(1.5f);
		this.setResistance(1.0f);
		this.setLightLevel(1.0f);

		this.setDefaultState(this.getTorchState(true));
	}

	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list)
	{
		list.add(new ItemStack(this,1, this.getMetaFromState(this.getTorchState(true))));
		list.add(new ItemStack(this, 1, this.getMetaFromState(this.getTorchState(false))));
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityMegaTorch();
	}

	@Override
	public Class<? extends TileEntity> getTileEntityClass()
	{
		return TileEntityMegaTorch.class;
	}

	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return state.getValue(BURNING);
	}

	@Override
	public String getTileEntityRegistryName()
	{
		return this.getRegistryName().toString();
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
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
	{
		super.onBlockAdded(worldIn, pos, state);

		ITorchRegistryContainer container = worldIn.getCapability(ModCaps.TORCH_REGISTRY_CONTAINER, null);
		if(container != null)
			container.getMegaTorchRegistry().register(pos);
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		super.breakBlock(worldIn, pos, state);

		ITorchRegistryContainer container = worldIn.getCapability(ModCaps.TORCH_REGISTRY_CONTAINER, null);
		if(container != null)
			container.getMegaTorchRegistry().unregister(pos);
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		if (!worldIn.isRemote && state.getValue(BURNING))
		{
			if(!stack.hasTagCompound()) return;

			TileEntity tile = worldIn.getTileEntity(pos);
			if(!(tile instanceof TileEntityMegaTorch)) return;

			NBTTagCompound compound = stack.getSubCompound("tm_tile");
			if(compound != null)
				((TileEntityMegaTorch) tile).readSyncNbt(compound);
		}
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if(worldIn.isRemote)
		{
			TorchVolumeRenderHandler.DyeColor color = TorchVolumeRenderHandler.DyeColor.FromItemStack(playerIn.getHeldItem(hand));
			if(color != null)
				TorchVolumeRenderHandler.toggle(pos, color);
		}

		if(state.getValue(BURNING))
		{
			playerIn.sendStatusMessage(new TextComponentTranslation(this.getTranslationKey() + ".already_lit"), true);
			return true;
		}
		if(worldIn.isRemote) return true;

		ItemStack itemStack = playerIn.getHeldItem(hand);
		if(itemStack.isEmpty()) return true;

		String itemId = itemStack.getItem().getRegistryName().toString();
		if(Arrays.stream(TorchmasterConfig.MegaTorchLighterItems).noneMatch(s -> s.equals(itemId))) return false;

		NBTTagCompound nbt = itemStack.getSubCompound("tm_lighter");
		int amount = 1;
		float chance = 1f;
		if(nbt != null)
		{
			amount = nbt.getInteger("amount");
		}

		if(itemStack.isItemStackDamageable())
		{
			int remainingDamage = 1 + itemStack.getMaxDamage() - itemStack.getItemDamage(); // item breaks only if damage > maxDamage, hence why we need to add +1
			chance = (float)remainingDamage / amount;
			itemStack.damageItem(amount, playerIn);
		}
		else
		{
			if(amount > itemStack.getCount())
			{
				playerIn.sendStatusMessage(new TextComponentTranslation(this.getTranslationKey() + ".light_failed_itemcount"), true);
				return false;
			}
			itemStack.shrink(amount);
		}

		if(worldIn.rand.nextFloat() > chance)
		{
			playerIn.sendStatusMessage(new TextComponentTranslation(this.getTranslationKey() + ".light_failed_itemtoodamaged"), false);
			return false;
		}

		worldIn.setBlockState(pos, this.getTorchState(true));

		TileEntity torchTe = worldIn.getTileEntity(pos);
		if(torchTe instanceof TileEntityMegaTorch)
		{
			((TileEntityMegaTorch) torchTe).relightTorch(TorchmasterConfig.MegaTorchBurnoutValue);
			worldIn.playSound(null, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0f, worldIn.rand.nextFloat() * 0.4F + 0.8F);
		}
		else
		{
			TorchMasterMod.Log.error("There is an error whith the MegaTorch @ " + pos + ". Please replace the block!");
		}
		return true;
	}

	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		// no fire for you if the torch isnt burning :x
		if(!stateIn.getValue(BURNING)) return;

		double d0 = (double) pos.getX() + 0.5D;
		double d1 = (double) pos.getY() + 1.1D;
		double d2 = (double) pos.getZ() + 0.5D;

		worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, 0.0D, 0.0D, 0.0D);
		worldIn.spawnParticle(EnumParticleTypes.FLAME, d0, d1, d2, 0.0D, 0.0D, 0.0D);
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, BURNING);
	}

	public IBlockState getTorchState(boolean isLit) { return this.getDefaultState().withProperty(BURNING, isLit); }

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(BURNING) ? 0 : 1;
	}

	@SuppressWarnings("deprecation")
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getTorchState(meta == 0);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerItemModels(Item item)
	{
		ModelLoader.setCustomModelResourceLocation(item, this.getMetaFromState(this.getTorchState(true)), new ModelResourceLocation(this.getRegistryName(), "burning=true"));
		ModelLoader.setCustomModelResourceLocation(item, this.getMetaFromState(this.getTorchState(false)), new ModelResourceLocation(this.getRegistryName(), "burning=false"));
	}

	@Override
	public ItemBlock createItemBlock()
	{
		return (ItemBlock) new ItemBlockMegaTorch(this).setRegistryName(this.getRegistryName());
	}

	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return state.getValue(BURNING) ? 15 : 0;
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
	{
		return new ItemStack(Item.getItemFromBlock(this), 1, this.getMetaFromState(state));
	}

	@Override
	public int damageDropped(IBlockState state)
	{
		return this.getMetaFromState(TorchmasterConfig.MegaTorchExtinguishOnHarvest ? this.getTorchState(false) : state);
	}

	@Override
	public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player)
	{
		return TorchmasterConfig.MegaTorchAllowSilkTouch;
	}

	@Override
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack)
	{
		if (te instanceof TileEntityMegaTorch)
		{
			if (worldIn.isRemote) return;
			if (!TorchmasterConfig.MegaTorchExtinguishOnHarvest || this.canSilkHarvest(worldIn, pos, state, player) && EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) > 0)
			{
				ItemStack itemStack = this.getSilkTouchDrop(state);
				if(!itemStack.isEmpty())
				{
					//noinspection ConstantConditions | this will never be null when we are getting called - otherwise, its a MC bug
					player.addStat(StatList.getBlockStats(this));
					player.addExhaustion(0.005F);

					NBTTagCompound compound = itemStack.getOrCreateSubCompound("tm_tile");
					((TileEntityMegaTorch) te).writeSyncNbt(compound);
					spawnAsEntity(worldIn, pos, itemStack);
					return;
				}
			}
		}
		super.harvestBlock(worldIn, player, pos, state, te, stack);
	}
}
