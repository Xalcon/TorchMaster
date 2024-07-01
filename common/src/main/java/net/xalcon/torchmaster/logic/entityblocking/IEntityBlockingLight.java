package net.xalcon.torchmaster.logic.entityblocking;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public interface IEntityBlockingLight
{
    boolean shouldBlockEntity(Entity entity, Level level, MobSpawnType spawnType);
    boolean shouldBlockVillagePillagerSiege(Vec3 pos);
    boolean shouldBlockVillageZombieRaid(Vec3 pos);

    String getLightSerializerType();
    String getDisplayName();
    BlockPos getPos();

    /**
     * Called frequently for cleanup purposes
     * Use this to check if the light should be removed from the registry, i.e. because a block no longer exists, etc
     * @param level the world
     * @return true if this instance should be removed from the registry, otherwise false.
     */
    boolean cleanupCheck(Level level);
}
