package net.xalcon.torchmaster.common.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.xalcon.torchmaster.common.items.ItemBlockTooltipInfo;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockInvisibleLight extends BlockBase
{
    private final static PropertyBool DECAY = PropertyBool.create("decay");

    public static final String INTERNAL_NAME = "invisible_light";
    private IBlockState decayState;

    public BlockInvisibleLight()
    {
        super(Material.AIR, INTERNAL_NAME);
        this.setTickRandomly(true);
        this.setLightLevel(1);
        this.setDefaultState(this.getDefaultState().withProperty(DECAY, false));
        this.decayState = this.getDefaultState().withProperty(DECAY, true);
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, DECAY);
    }

    @Override @SuppressWarnings("deprecation")
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(DECAY, meta != 0);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(DECAY) ? 1 : 0;
    }

    @Override @SuppressWarnings("deprecation")
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.INVISIBLE;
    }

    @Nullable
    @Override @SuppressWarnings("deprecation")
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
        return NULL_AABB;
    }

    @Override @SuppressWarnings("deprecation")
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean canCollideCheck(IBlockState state, boolean hitIfLiquid)
    {
        return false;
    }

    @Override
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)
    {
    }

    @Override
    public int tickRate(World worldIn)
    {
        return 0;
    }

    @Override
    public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos)
    {
        return true;
    }

    @Override @SuppressWarnings("deprecation")
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override @SuppressWarnings("deprecation")
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if(state.getValue(DECAY))
            worldIn.setBlockToAir(pos);
    }

    public IBlockState getDecayState()
    {
        return this.decayState;
    }

    @Override
    public Item createItemBlock()
    {
        return null;
    }
}
