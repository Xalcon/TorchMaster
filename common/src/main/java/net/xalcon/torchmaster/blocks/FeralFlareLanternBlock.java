package net.xalcon.torchmaster.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
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
import net.xalcon.torchmaster.ModRegistry;

import javax.annotation.Nullable;

public class FeralFlareLanternBlock extends DirectionalBlock implements EntityBlock
{
    protected static final VoxelShape SHAPE = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 16.0D, 12.0D);
    public static final MapCodec<FeralFlareLanternBlock> CODEC = simpleCodec(FeralFlareLanternBlock::new);

    public FeralFlareLanternBlock(Properties properties)
    {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.SOUTH));
    }

    @Override
    protected MapCodec<? extends DirectionalBlock> codec()
    {
        return CODEC;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
    {
        return SHAPE;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getClickedFace().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean moving)
    {
        if(level.getBlockEntity(pos) instanceof FeralFlareLanternBlockEntity te)
            te.removeChildLights();

        super.onRemove(state, level, pos, oldState, moving);
    }

    @Override
    public void destroy(LevelAccessor level, BlockPos pos, BlockState state)
    {
        if(level.getBlockEntity(pos) instanceof FeralFlareLanternBlockEntity te)
            te.removeChildLights();

        super.destroy(level, pos, state);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FeralFlareLanternBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return type == ModRegistry.tileFeralFlareLantern.get() ? FeralFlareLanternBlockEntity::dispatchTickBlockEntity : null;
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
