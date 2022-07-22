package net.xalcon.torchmaster.events;

import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.xalcon.torchmaster.Constants;
import net.xalcon.torchmaster.platform.Services;

import java.util.function.BooleanSupplier;

public class EventHandler
{
    public void onCheckSpawn(final MobSpawnType spawnType, final Entity entity, final Vec3 location, final EventResultContainer container)
    {
        var config = Services.PLATFORM.getConfig();
        boolean log = config.getLogSpawnChecks();
        if (log) Constants.LOG.debug("DoSpecialSpawn - Reason: {}, Type: {}, Pos: {}/{}/{}", spawnType, EntityType.getKey(entity.getType()), location.x, location.y, location.z);
        // Respect other mods decisions to allow a spawn if enabled in config
        if(!config.getAggressiveSpawnChecks() && container.getResult() == EventResult.ALLOW) return;
        // check if non-natural spawns should be blocked
        if(config.getBlockOnlyNaturalSpawns() && spawnType == MobSpawnType.SPAWNER) return;
        if(spawnType != MobSpawnType.NATURAL && spawnType != MobSpawnType.STRUCTURE) return;

        var level = entity.getCommandSenderWorld();

        var pos = new BlockPos(location);

        Services.PLATFORM.getRegistryForLevel(level).ifPresent(reg ->
        {
            if(reg.shouldBlockEntity(entity, pos))
            {
                container.setResult(EventResult.DENY);
                if (log) Constants.LOG.debug("Blocking spawn of {}", EntityType.getKey(entity.getType()));
                //event.getEntity().addTag("torchmaster_removed_spawn");
            }
            else
            {
                if (log) Constants.LOG.debug("Allowed spawn of {}", EntityType.getKey(entity.getType()));
            }
        });
    }

    public void onDoSpecialSpawn(final MobSpawnType spawnType, final Entity entity, final Vec3 location, final EventResultContainer container)
    {
        var config = Services.PLATFORM.getConfig();
        boolean log = config.getLogSpawnChecks();
        if (log) Constants.LOG.debug("DoSpecialSpawn - Reason: {}, Type: {}, Pos: {}/{}/{}", spawnType, EntityType.getKey(entity.getType()), location.x, location.y, location.z);
        // Respect other mods decisions to allow a spawn if enabled in config
        if(!config.getAggressiveSpawnChecks() && container.getResult() == EventResult.ALLOW) return;
        // check if non-natural spawns should be blocked
        if(config.getBlockOnlyNaturalSpawns() && spawnType == MobSpawnType.SPAWNER) return;
        if(spawnType != MobSpawnType.NATURAL && spawnType != MobSpawnType.STRUCTURE) return;

        var world = entity.getCommandSenderWorld();
        var pos = new BlockPos(location);

        Services.PLATFORM.getRegistryForLevel(world).ifPresent(reg ->
        {
            if(reg.shouldBlockEntity(entity, pos))
            {
                container.setResult(EventResult.DENY);
                if (log) Constants.LOG.debug("Blocking spawn of {}", EntityType.getKey(entity.getType()));
            }
            else
            {
                if (log) Constants.LOG.debug("Allowed spawn of {}", EntityType.getKey(entity.getType()));
            }
        });
    }

    public void onVillageSiege(Level level, Vec3 attemptedSpawnPos, EventResultContainer container)
    {
        var config = Services.PLATFORM.getConfig();
        if(!config.getBlockVillageSieges()) return;
        boolean log = config.getLogSpawnChecks();
        if (log) Constants.LOG.debug("VillageSiegeEvent - Pos: {}", attemptedSpawnPos);
        if(!config.getAggressiveSpawnChecks() && container.getResult() == EventResult.ALLOW) return;

        var pos = new BlockPos(attemptedSpawnPos);

        Services.PLATFORM.getRegistryForLevel(level).ifPresent(reg ->
        {
            if(reg.shouldBlockVillageSiege(pos))
            {
                container.setResult(EventResult.DENY);
                if (log) Constants.LOG.debug("Blocking village siege @ {}", pos);
            }
            else
            {
                if (log) Constants.LOG.debug("Allowed village siege @ {}", pos);
            }
        });
    }

    public void onServerLevelTickEnd(MinecraftServer server, BooleanSupplier haveTime)
    {
        for(ServerLevel level : server.getAllLevels())
        {
            Services.PLATFORM.getRegistryForLevel(level).ifPresent(reg -> reg.onGlobalTick(level));
        }
    }
}
