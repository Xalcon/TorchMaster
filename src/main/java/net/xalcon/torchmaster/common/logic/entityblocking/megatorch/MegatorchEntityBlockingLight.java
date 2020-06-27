package net.xalcon.torchmaster.common.logic.entityblocking.megatorch;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;
import net.xalcon.torchmaster.Torchmaster;
import net.xalcon.torchmaster.TorchmasterConfig;
import net.xalcon.torchmaster.common.ModBlocks;
import net.xalcon.torchmaster.common.logic.DistanceLogics;
import net.xalcon.torchmaster.common.logic.entityblocking.IEntityBlockingLight;

public class MegatorchEntityBlockingLight implements IEntityBlockingLight
{
    public static final VoxelShape SHAPE = Block.makeCuboidShape(6.0D, 0.0D, 6.0D, 10.0D, 16.0D, 10.0D);
    private BlockPos pos;

    public MegatorchEntityBlockingLight(BlockPos pos)
    {
        this.pos = pos;
    }

    @Override
    public boolean shouldBlockEntity(Entity entity)
    {
        return Torchmaster.MegaTorchFilterRegistry.containsEntity(entity.getType().getRegistryName())
            && DistanceLogics.Cubic.isPositionInRange(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ, pos, TorchmasterConfig.GENERAL.megaTorchRadius.get());
    }

    @Override
    public String getLightSerializerKey()
    {
        return MegatorchSerializer.SERIALIZER_KEY;
    }

    /**
     * Called frequently for cleanup purposes
     * Use this to check if the light should be removed from the registry, i.e. because a block no longer exists, etc
     *
     * @param world the world
     * @return true if this instance should be removed from the registry, otherwise false.
     */
    @Override
    public boolean cleanupCheck(World world)
    {
        return world.isBlockLoaded(this.pos) && world.getBlockState(pos).getBlock() != ModBlocks.blockMegaTorch;
    }

    @Override
    public String getName()
    {
        return "Mega Torch";
    }

    @Override
    public BlockPos getPos()
    {
        return pos;
    }
}
