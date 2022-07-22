package net.xalcon.torchmaster.logic.entityblocking.dreadlamp;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.xalcon.torchmaster.ModRegistry;
import net.xalcon.torchmaster.Torchmaster;
import net.xalcon.torchmaster.logic.IDistanceLogic;
import net.xalcon.torchmaster.logic.entityblocking.IEntityBlockingLight;
import net.xalcon.torchmaster.platform.Services;

public class DreadLampEntityBlockingLight implements IEntityBlockingLight
{
    public static final VoxelShape SHAPE = Block.box(1, 1, 1, 15, 15, 15);
    private final BlockPos pos;

    public DreadLampEntityBlockingLight(BlockPos pos)
    {
        this.pos = pos;
    }

    @Override
    public boolean shouldBlockEntity(Entity entity, BlockPos pos)
    {
        return Torchmaster.DreadLampFilterRegistry.containsEntity(EntityType.getKey(entity.getType()))
            && IDistanceLogic.Cubic.isPositionInRange(pos.getX(), pos.getY(), pos.getZ(), this.pos, Services.PLATFORM.getConfig().getDreadLampRadius());
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
        return level.isLoaded(this.pos) && level.getBlockState(pos).getBlock() != ModRegistry.blockDreadLamp.get();
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
