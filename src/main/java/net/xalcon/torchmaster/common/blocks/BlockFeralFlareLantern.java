package net.xalcon.torchmaster.common.blocks;

import akka.dispatch.sysmsg.Create;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.xalcon.torchmaster.TorchMasterMod;
import net.xalcon.torchmaster.common.ModGuiHandler;
import net.xalcon.torchmaster.common.tiles.IAutoRegisterTileEntity;
import net.xalcon.torchmaster.common.tiles.TileEntityFeralFlareLantern;

import javax.annotation.Nullable;

public class BlockFeralFlareLantern extends BlockBase implements IAutoRegisterTileEntity
{
    public final static String INTERNAL_NAME = "feral_flare_lantern";

    public final static IProperty<EnumFacing> FACING = PropertyEnum.create("facing", EnumFacing.class);

    public BlockFeralFlareLantern()
    {
        super(Material.WOOD, INTERNAL_NAME);
        this.setDefaultState(this.getDefaultState().withProperty(FACING, EnumFacing.DOWN));
        this.setLightLevel(1);
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING);
    }

    @Override @SuppressWarnings("deprecation")
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(FACING, EnumFacing.byIndex(meta & 0b0111));
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(FACING).getIndex();
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
    {
        return this.getDefaultState().withProperty(FACING, facing.getOpposite());
    }

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new TileEntityFeralFlareLantern();
    }

    @Override
    public void registerItemModels(Item item)
    {
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(this.getRegistryName(), "facing=down"));
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        playerIn.openGui(TorchMasterMod.instance, ModGuiHandler.GuiType.FERAL_LANTERN.ordinal(), worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        TileEntity te = worldIn.getTileEntity(pos);
        if(te instanceof TileEntityFeralFlareLantern)
            ((TileEntityFeralFlareLantern) te).removeChildLights();
        super.breakBlock(worldIn, pos, state);
    }

    @Override @SuppressWarnings("deprecation")
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override @SuppressWarnings("deprecation")
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public Class<? extends TileEntity> getTileEntityClass()
    {
        return TileEntityFeralFlareLantern.class;
    }

    @Override
    public String getTileEntityRegistryName()
    {
        return TorchMasterMod.MODID + ":" + INTERNAL_NAME;
    }
}
