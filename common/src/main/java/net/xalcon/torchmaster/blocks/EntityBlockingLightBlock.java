package net.xalcon.torchmaster.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.xalcon.torchmaster.Torchmaster;
import net.xalcon.torchmaster.client.MegaTorchScreenOpener;

public class EntityBlockingLightBlock extends Block
{
    public static final BooleanProperty OVERLAY_VISIBLE = BooleanProperty.create("overlay_visible");

    private final LightType lightType;

    public EntityBlockingLightBlock(Properties properties, LightType lightType)
    {
        super(properties);
        this.lightType = lightType;
        this.registerDefaultState(this.stateDefinition.any().setValue(OVERLAY_VISIBLE, false));
    }

    public LightType getLightType()
    {
        return this.lightType;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(OVERLAY_VISIBLE);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext ctx) {
        return lightType.Shape;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource randomSource)
    {
        double d0 = (double)pos.getX() + lightType.FlameOffset.x;
        double d1 = (double)pos.getY() + lightType.FlameOffset.y;
        double d2 = (double)pos.getZ() + lightType.FlameOffset.z;
        level.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0f, 0.0f, 0.0f);
        level.addParticle(ParticleTypes.FLAME, d0, d1, d2, 0.0f, 0.0f, 0.0f);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit)
    {
        if (this.lightType != LightType.MegaTorch)
            return InteractionResult.PASS;

        if (level.isClientSide)
        {
            MegaTorchScreenOpener.open(pos);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.CONSUME;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        if (this.lightType != LightType.MegaTorch || player.isShiftKeyDown())
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        if (level.isClientSide)
        {
            MegaTorchScreenOpener.open(pos);
            return ItemInteractionResult.SUCCESS;
        }
        return ItemInteractionResult.CONSUME;
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean moving) {
        super.onPlace(state, level, pos, oldState, moving);
        Torchmaster.getRegistryForLevel(level)
            .ifPresent(reg ->
                    reg.registerLight(lightType.KeyFactory.apply(pos), lightType.LightFactory.apply(pos)));
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter getter, BlockPos pos) {
        return true;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean moving) {
        Torchmaster.getRegistryForLevel(level)
            .ifPresent(reg ->
                    reg.unregisterLight(lightType.KeyFactory.apply(pos)));
        super.onRemove(state, level, pos, oldState, moving);
    }
}
