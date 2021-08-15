package net.xalcon.torchmaster.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.xalcon.torchmaster.common.ModCaps;
import net.xalcon.torchmaster.common.logic.entityblocking.IEntityBlockingLight;
import net.xalcon.torchmaster.common.logic.entityblocking.megatorch.MegatorchEntityBlockingLight;

import java.util.Random;
import java.util.function.Function;

public class EntityBlockingLightBlock extends Block
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
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext ctx) {
        return this.shape;
    }

    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, Level level, BlockPos pos, Random rand)
    {
        double d0 = (double)pos.getX() + 0.5f;
        double d1 = (double)pos.getY() + this.flameOffsetY;
        double d2 = (double)pos.getZ() + 0.5f;
        level.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0f, 0.0f, 0.0f);
        level.addParticle(ParticleTypes.FLAME, d0, d1, d2, 0.0f, 0.0f, 0.0f);
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
}
