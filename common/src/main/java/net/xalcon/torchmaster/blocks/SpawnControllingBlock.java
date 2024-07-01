package net.xalcon.torchmaster.blocks;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.xalcon.torchmaster.platform.Services;

import java.util.function.Function;

public class SpawnControllingBlock extends Block
{
    private final LightType type;

    public SpawnControllingBlock(Properties properties, LightType type)
    {
        super(properties);
        this.type = type;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext ctx) {
        return type.Shape;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource randomSource)
    {
        var x = type.FlameOffset.x + pos.getX();
        var y = type.FlameOffset.y + pos.getY();
        var z = type.FlameOffset.z + pos.getZ();

        level.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0f, 0.01f, 0.0f);
        level.addParticle(ParticleTypes.FLAME, x, y, z, 0.0f, 0.0f, 0.0f);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean moving) {
        super.onPlace(state, level, pos, oldState, moving);
        // TODO: REGISTER ON PLACE
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter getter, BlockPos pos) {
        return true;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean moving) {
        // TODO: UNREGISTER ON REMOVE
        super.onRemove(state, level, pos, oldState, moving);
    }
}
