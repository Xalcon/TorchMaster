package net.xalcon.torchmaster.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.xalcon.torchmaster.Torchmaster;

public class EntityBlockingLightBlock extends Block
{
    private final LightType lightType;

    public EntityBlockingLightBlock(Properties properties, LightType lightType)
    {
        super(properties);
        this.lightType = lightType;
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
