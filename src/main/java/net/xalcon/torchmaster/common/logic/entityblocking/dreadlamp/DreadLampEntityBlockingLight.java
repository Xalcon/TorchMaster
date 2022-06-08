package net.xalcon.torchmaster.common.logic.entityblocking.dreadlamp;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.xalcon.torchmaster.Torchmaster;
import net.xalcon.torchmaster.TorchmasterConfig;
import net.xalcon.torchmaster.common.ModBlocks;
import net.xalcon.torchmaster.common.logic.DistanceLogics;
import net.xalcon.torchmaster.common.logic.entityblocking.IEntityBlockingLight;

public class DreadLampEntityBlockingLight implements IEntityBlockingLight
{
    public static final VoxelShape SHAPE = Block.box(1, 1, 1, 15, 15, 15);
    private BlockPos pos;

    public DreadLampEntityBlockingLight(BlockPos pos)
    {
        this.pos = pos;
    }

    @Override
    public boolean shouldBlockEntity(Entity entity, BlockPos pos)
    {
        return Torchmaster.DreadLampFilterRegistry.containsEntity(EntityType.getKey(entity.getType()))
            && DistanceLogics.Cubic.isPositionInRange(pos.getX(), pos.getY(), pos.getZ(), this.pos, TorchmasterConfig.GENERAL.dreadLampRadius.get());
    }

    @Override
    public boolean shouldBlockVillageSiege(BlockPos pos)
    {
        return false;
    }

    @Override
    public String getLightSerializerKey()
    {
        return DreadLampSerializer.SERIALIZER_KEY;
    }

    @Override
    public boolean cleanupCheck(Level level)
    {
        return level.isLoaded(this.pos) && level.getBlockState(pos).getBlock() != ModBlocks.blockDreadLamp.get();
    }

    @Override
    public String getName()
    {
        return "Dread Lamp";
    }

    @Override
    public BlockPos getPos()
    {
        return pos;
    }
}
