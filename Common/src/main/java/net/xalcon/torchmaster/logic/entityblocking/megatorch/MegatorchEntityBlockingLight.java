package net.xalcon.torchmaster.logic.entityblocking.megatorch;

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

public class MegatorchEntityBlockingLight implements IEntityBlockingLight
{
    public static final VoxelShape SHAPE = Block.box(6.0D, 0.0D, 6.0D, 10.0D, 16.0D, 10.0D);
    private BlockPos pos;

    public MegatorchEntityBlockingLight(BlockPos pos)
    {
        this.pos = pos;
    }

    @Override
    public boolean shouldBlockEntity(Entity entity, BlockPos pos)
    {
        return Torchmaster.MegaTorchFilterRegistry.containsEntity(EntityType.getKey(entity.getType()))
            && IDistanceLogic.Cubic.isPositionInRange(pos.getX(), pos.getY(), pos.getZ(), this.pos, Services.PLATFORM.getConfig().getMegaTorchRadius());
    }

    @Override
    public boolean shouldBlockVillageSiege(BlockPos pos)
    {
        return IDistanceLogic.Cubic.isPositionInRange(pos.getX(), pos.getY(), pos.getZ(), this.pos, Services.PLATFORM.getConfig().getMegaTorchRadius());
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
     * @param level the world
     * @return true if this instance should be removed from the registry, otherwise false.
     */
    @Override
    public boolean cleanupCheck(Level level)
    {
        return level.isLoaded(this.pos) && level.getBlockState(pos).getBlock() != ModRegistry.blockMegaTorch.get();
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
