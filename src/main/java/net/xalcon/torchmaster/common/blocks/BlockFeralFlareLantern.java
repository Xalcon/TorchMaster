package net.xalcon.torchmaster.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.xalcon.torchmaster.Torchmaster;
import net.xalcon.torchmaster.common.tiles.TileEntityFeralFlareLantern;

import javax.annotation.Nullable;

public class BlockFeralFlareLantern extends Block implements ITileEntityProvider
{
    public final static String INTERNAL_NAME = "feral_flare_lantern";

    public final static IProperty<EnumFacing> FACING = EnumProperty.create("facing", EnumFacing.class);

    public BlockFeralFlareLantern()
    {
        super(Builder.create(Material.WOOD).lightValue(1));
        this.setDefaultState(this.getDefaultState().with(FACING, EnumFacing.DOWN));
        this.setRegistryName(Torchmaster.MODID, INTERNAL_NAME);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder)
    {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public IBlockState getStateForPlacement(BlockItemUseContext ctx)
    {
        return this.getDefaultState().with(FACING, ctx.getFace().getOpposite());
    }

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(IBlockState state, IBlockReader world)
    {
        return new TileEntityFeralFlareLantern();
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader iBlockReader)
    {
        return new TileEntityFeralFlareLantern();
    }

    @Override
    public boolean onBlockActivated(IBlockState state, World world, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing facing, float x, float y, float z)
    {
        // TODO: GUI
        //playerIn.openGui(TorchMasterMod.instance, ModGuiHandler.GuiType.FERAL_LANTERN.ordinal(), worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @Override
    public void onReplaced(IBlockState state, World world, BlockPos pos, IBlockState newState, boolean isMoving)
    {
        TileEntity te = world.getTileEntity(pos);
        if(te instanceof TileEntityFeralFlareLantern)
            ((TileEntityFeralFlareLantern) te).removeChildLights();
        super.onReplaced(state, world, pos, newState, isMoving);
    }

    @Override @SuppressWarnings("deprecation")
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }
}
