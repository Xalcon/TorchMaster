package net.xalcon.torchmaster.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.xalcon.torchmaster.common.ModBlocks;
import net.xalcon.torchmaster.common.tiles.FeralFlareLanternTileEntity;

import javax.annotation.Nullable;

public class FeralFlareLanternBlock extends DirectionalBlock implements EntityBlock
{
    protected static final VoxelShape SHAPE = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 16.0D, 12.0D);

    public FeralFlareLanternBlock(Properties properties)
    {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.SOUTH));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
    {
        return SHAPE;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot)
    {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean moving) {
        var te = world.getBlockEntity(pos);
        if(te instanceof FeralFlareLanternTileEntity)
            ((FeralFlareLanternTileEntity) te).removeChildLights();

        super.onRemove(state, world, pos, oldState, moving);
    }



    @Override
    public void destroy(LevelAccessor level, BlockPos pos, BlockState state)
    {
        var te = level.getBlockEntity(pos);
        if(te instanceof FeralFlareLanternTileEntity)
            ((FeralFlareLanternTileEntity) te).removeChildLights();
        super.destroy(level, pos, state);
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @org.jetbrains.annotations.Nullable BlockEntity te, ItemStack itemStack)
    {
        if(te instanceof FeralFlareLanternTileEntity)
            ((FeralFlareLanternTileEntity) te).removeChildLights();
        super.playerDestroy(level, player, pos, state, te, itemStack);
    }

    @Override
    public void wasExploded(Level level, BlockPos pos, Explosion explosion)
    {
        var te = level.getBlockEntity(pos);
        if(te instanceof FeralFlareLanternTileEntity)
            ((FeralFlareLanternTileEntity) te).removeChildLights();
        super.wasExploded(level, pos, explosion);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FeralFlareLanternTileEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return type == ModBlocks.tileFeralFlareLantern.get() ? FeralFlareLanternTileEntity::dispatchTickBlockEntity : null;
    }



    //@Override
    //public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
    //    if(world.isClientSide) return InteractionResult.SUCCESS;
    //    var tile = world.getBlockEntity(pos);
    //    if(tile instanceof FeralFlareLanternTileEntity lantern)
    //    {
    //        lantern.setUseLineOfSight(!lantern.shouldUseLineOfSight());
    //        if(lantern.shouldUseLineOfSight())
    //            player.displayClientMessage(new TranslatableComponent("tile.feral_flare_lantern.line_of_sight.enabled"), true);
    //        else
    //            player.displayClientMessage(new TranslatableComponent("tile.feral_flare_lantern.line_of_sight.disabled"), true);
    //    }
    //    return InteractionResult.SUCCESS;
    //}
}
