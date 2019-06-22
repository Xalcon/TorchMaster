package net.xalcon.torchmaster.common.logic.entityblocking;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public interface IEntityBlockingLight
{
    boolean shouldBlockEntity(Entity entity);
    String getLightSerializerKey();

    /**
     * Called frequently for cleanup purposes
     * Use this to check if the light should be removed from the registry, i.e. because a block no longer exists, etc
     * @param world the world
     * @return true if this instance should be removed from the registry, otherwise false.
     */
    boolean cleanupCheck(World world);
}
