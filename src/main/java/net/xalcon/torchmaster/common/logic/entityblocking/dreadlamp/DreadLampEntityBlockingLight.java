package net.xalcon.torchmaster.common.logic.entityblocking.dreadlamp;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.xalcon.torchmaster.Torchmaster;
import net.xalcon.torchmaster.TorchmasterConfig;
import net.xalcon.torchmaster.common.ModBlocks;
import net.xalcon.torchmaster.common.logic.DistanceLogics;
import net.xalcon.torchmaster.common.logic.entityblocking.IEntityBlockingLight;

public class DreadLampEntityBlockingLight implements IEntityBlockingLight
{
    private BlockPos pos;

    public DreadLampEntityBlockingLight(BlockPos pos)
    {
        this.pos = pos;
    }

    @Override
    public boolean shouldBlockEntity(Entity entity)
    {
        return Torchmaster.DreadLampFilterRegistry.containsEntity(entity.getType().getRegistryName())
            && DistanceLogics.Cubic.isPositionInRange(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ, pos, TorchmasterConfig.GENERAL.dreadLampRadius.get());
    }

    @Override
    public String getLightSerializerKey()
    {
        return DreadLampSerializer.SERIALIZER_KEY;
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
