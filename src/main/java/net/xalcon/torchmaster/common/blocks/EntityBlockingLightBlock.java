package net.xalcon.torchmaster.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.sound.SoundEvent;
import net.xalcon.torchmaster.TorchmasterConfig;
import net.xalcon.torchmaster.common.ModBlocks;
import net.xalcon.torchmaster.common.ModCaps;
import net.xalcon.torchmaster.common.logic.entityblocking.IEntityBlockingLight;
import net.xalcon.torchmaster.common.logic.entityblocking.megatorch.MegatorchEntityBlockingLight;
import net.xalcon.torchmaster.common.tiles.FeralFlareLanternTileEntity;
import net.xalcon.torchmaster.common.tiles.MegatorchTileEntity;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.function.Function;

public class EntityBlockingLightBlock extends Block implements EntityBlock
{
    protected static final VoxelShape SHAPE =  Block.box(6.0D, 0.0D, 6.0D, 10.0D, 16.0D, 10.0D);

    private Function<BlockPos, String> keyFactory;
    private Function<BlockPos, IEntityBlockingLight> lightFactory;
    private float flameOffsetY;
    private final VoxelShape shape;

    public EntityBlockingLightBlock(Properties properties, Function<BlockPos, String> keyFactory, Function<BlockPos, IEntityBlockingLight> lightFactory, float flameOffsetY, VoxelShape shape)
    {
        super(properties);
        this.keyFactory = keyFactory;
        this.lightFactory = lightFactory;
        this.flameOffsetY = flameOffsetY;
        this.shape = shape;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext ctx)
    {
        return this.shape;
    }

    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, Level level, BlockPos pos, Random rand)
    {
        var warn = false;
        if(TorchmasterConfig.GENERAL.enableMegatorchBurnout.get())
        {
            var tile = level.getBlockEntity(pos, ModBlocks.tileMegaTorch);
            if(tile.isPresent())
            {
                if(tile.get().isEnabled()) return;
                var fuelLevel = tile.get().getFuelLevel();
                if(fuelLevel < TorchmasterConfig.GENERAL.megatorchBurnoutWarningThreshold.get())
                {
                    if(level.getGameTime() % 20 == 0 && rand.nextFloat() > .8f)
                    {
                        warn = true;
                    }
                }
            }
        }

        double d0 = (double)pos.getX() + 0.5f;
        double d1 = (double)pos.getY() + this.flameOffsetY;
        double d2 = (double)pos.getZ() + 0.5f;
        level.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0f, 0.0f, 0.0f);

        if(warn)
        {
            level.playLocalSound(d0, d1, d2, SoundEvents.BLAZE_BURN, SoundSource.BLOCKS, 1.0F + rand.nextFloat(), rand.nextFloat() * 0.7F + 0.3F, false);
        }
        else
        {
            level.addParticle(ParticleTypes.FLAME, d0, d1, d2, 0.0f, 0.0f, 0.0f);
        }
    }

    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean moving) {
        super.onPlace(state, world, pos, oldState, moving);
        world.getCapability(ModCaps.TEB_REGISTRY)
            .ifPresent(reg ->
            {
                reg.registerLight(this.keyFactory.apply(pos), this.lightFactory.apply(pos));
            });
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter getter, BlockPos pos) {
        return true;
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean moving) {
        world.getCapability(ModCaps.TEB_REGISTRY)
            .ifPresent(reg ->
            {
                reg.unregisterLight(this.keyFactory.apply(pos));
            });
        super.onRemove(state, world, pos, oldState, moving);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult blockHitResult)
    {
        if(level.isClientSide()) return InteractionResult.PASS;

        if(!TorchmasterConfig.GENERAL.enableMegatorchBurnout.get()) return InteractionResult.PASS;

        var item = player.getItemInHand(hand);
        if(item.isEmpty()) return InteractionResult.PASS;

        var tile = level.getBlockEntity(pos, ModBlocks.tileMegaTorch);
        if(tile.isPresent())
        {
            if(tile.get().refuel(player, item))
                return InteractionResult.CONSUME;
        }
        return super.use(state, level, pos, player, hand, blockHitResult);
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        if(state.is(ModBlocks.blockMegaTorch))
            return new MegatorchTileEntity(pos, state);
        return null;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type)
    {
        var enabled = TorchmasterConfig.GENERAL.enableMegatorchBurnout.get();
        return enabled && type == ModBlocks.tileMegaTorch ? MegatorchTileEntity::dispatchTickBlockEntity : null;
    }
}
