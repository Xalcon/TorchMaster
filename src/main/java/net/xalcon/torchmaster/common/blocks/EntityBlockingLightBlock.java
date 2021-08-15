package net.xalcon.torchmaster.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
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

    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return this.shape;
    }

    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        double d0 = (double)pos.getX() + 0.5f;
        double d1 = (double)pos.getY() + this.flameOffsetY;
        double d2 = (double)pos.getZ() + 0.5f;
        worldIn.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0f, 0.0f, 0.0f);
        worldIn.addParticle(ParticleTypes.FLAME, d0, d1, d2, 0.0f, 0.0f, 0.0f);
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean moving)
    {
        super.onBlockAdded(state, world, pos, oldState, moving);
        world.getCapability(ModCaps.TEB_REGISTRY)
        .ifPresent(reg ->
        {
            //String lightKey = pos.getX() + "_" + pos.getY() + "_" + pos.getZ();
            reg.registerLight(this.keyFactory.apply(pos), this.lightFactory.apply(pos));
        });
    }

    public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
        return true;
    }


    @Override
    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState oldState, boolean moving)
    {
        world.getCapability(ModCaps.TEB_REGISTRY)
        .ifPresent(reg ->
        {
            //String lightKey = pos.getX() + "_" + pos.getY() + "_" + pos.getZ();
            reg.unregisterLight(this.keyFactory.apply(pos));
        });
        super.onReplaced(state, world, pos, oldState, moving);
    }
}
