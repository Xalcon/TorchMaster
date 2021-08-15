package net.xalcon.torchmaster.common.blocks;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.DirectionalBlock;
import net.xalcon.torchmaster.common.tiles.FeralFlareLanternTileEntity;

import javax.annotation.Nullable;

public class FeralFlareLanternBlock extends DirectionalBlock
{
    //protected static final VoxelShape SHAPE = Block.makeCuboidShape(4.0D, 0.0D, 4.0D, 12.0D, 16.0D, 12.0D);
//
    public FeralFlareLanternBlock(Properties properties)
    {
        super(properties);
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.SOUTH));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return SHAPE;
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getNearestLookingDirection());
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot)
    {
        return state.with(FACING, rot.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.toRotation(state.get(FACING)));
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return new FeralFlareLanternTileEntity();
    }

    @Override
    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moving)
    {
        TileEntity te = world.getTileEntity(pos);
        if(te instanceof FeralFlareLanternTileEntity)
            ((FeralFlareLanternTileEntity) te).removeChildLights();

        super.onReplaced(state, world, pos, newState, moving);
    }
}
