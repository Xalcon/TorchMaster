package net.xalcon.torchmaster.common.logic.entityblocking;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public interface IEntityBlockingLight
{
    boolean shouldBlockEntity(Entity entity, BlockPos pos);
    String getLightSerializerKey();

    String getName();
    BlockPos getPos();

    /**
     * Called frequently for cleanup purposes
     * Use this to check if the light should be removed from the registry, i.e. because a block no longer exists, etc
     * @param level the world
     * @return true if this instance should be removed from the registry, otherwise false.
     */
    boolean cleanupCheck(Level level);
}
