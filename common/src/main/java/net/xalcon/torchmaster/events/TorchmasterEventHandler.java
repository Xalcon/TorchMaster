package net.xalcon.torchmaster.events;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.xalcon.torchmaster.Torchmaster;
import net.xalcon.torchmaster.platform.Services;

import java.util.function.BooleanSupplier;

public class TorchmasterEventHandler
{
    public static void onCheckSpawn(final MobSpawnType spawnType, final Entity entity, final Vec3 location, final EventResultContainer container)
    {
        var config = Services.PLATFORM.getConfig();
        boolean log = config.shouldLogSpawnChecks();
        if (log) Torchmaster.LOG.debug("CheckSpawn - Reason: {}, Type: {}, Pos: {}/{}/{}", spawnType, EntityType.getKey(entity.getType()), location.x, location.y, location.z);
        // Respect other mods decisions to allow a spawn if enabled in config
        if(!config.getAggressiveSpawnChecks() && container.getResult() == EventResult.ALLOW) return;
        // check if non-natural spawns should be blocked
        if(config.getBlockOnlyNaturalSpawns() && spawnType == MobSpawnType.SPAWNER) return;
        if(spawnType != MobSpawnType.NATURAL && spawnType != MobSpawnType.STRUCTURE) return;

        var level = entity.getCommandSenderWorld();

        Torchmaster.getRegistryForLevel(level).ifPresent(reg ->
        {
            if(reg.shouldBlockEntity(entity, entity.getCommandSenderWorld(), spawnType))
            {
                container.setResult(EventResult.DENY);
                if (log) Torchmaster.LOG.debug("Blocking spawn of {}", EntityType.getKey(entity.getType()));
                //event.getEntity().addTag("torchmaster_removed_spawn");
            }
            else
            {
                if (log) Torchmaster.LOG.debug("Allowed spawn of {}", EntityType.getKey(entity.getType()));
            }
        });
    }

    public static void onDoSpecialSpawn(final MobSpawnType spawnType, final Entity entity, final Vec3 location, final EventResultContainer container)
    {
        var config = Services.PLATFORM.getConfig();
        boolean log = config.shouldLogSpawnChecks();
        if (log) Torchmaster.LOG.debug("DoSpecialSpawn - Reason: {}, Type: {}, Pos: {}/{}/{}", spawnType, EntityType.getKey(entity.getType()), location.x, location.y, location.z);
        // Respect other mods decisions to allow a spawn if enabled in config
        if(!config.getAggressiveSpawnChecks() && container.getResult() == EventResult.ALLOW) return;
        // check if non-natural spawns should be blocked
        if(config.getBlockOnlyNaturalSpawns() && spawnType == MobSpawnType.SPAWNER) return;
        if(spawnType != MobSpawnType.NATURAL && spawnType != MobSpawnType.STRUCTURE) return;

        var world = entity.getCommandSenderWorld();

        Torchmaster.getRegistryForLevel(world).ifPresent(reg ->
        {
            if(reg.shouldBlockEntity(entity, entity.getCommandSenderWorld(), spawnType))
            {
                container.setResult(EventResult.DENY);
                if (log) Torchmaster.LOG.debug("Blocking spawn of {}", EntityType.getKey(entity.getType()));
            }
            else
            {
                if (log) Torchmaster.LOG.debug("Allowed spawn of {}", EntityType.getKey(entity.getType()));
            }
        });
    }

    public static void onVillageSiege(Level level, Vec3 attemptedSpawnPos, EventResultContainer container)
    {
        var config = Services.PLATFORM.getConfig();
        if(!config.getBlockVillageSieges()) return;
        boolean log = config.shouldLogSpawnChecks();
        if (log) Torchmaster.LOG.debug("VillageSiegeEvent - Pos: {}", attemptedSpawnPos);
        if(!config.getAggressiveSpawnChecks() && container.getResult() == EventResult.ALLOW) return;

        Torchmaster.getRegistryForLevel(level).ifPresent(reg ->
        {
            if(reg.shouldBlockVillagePillagerSiege(attemptedSpawnPos))
            {
                container.setResult(EventResult.DENY);
                if (log) Torchmaster.LOG.debug("Blocking village siege @ {}", attemptedSpawnPos);
            }
            else
            {
                if (log) Torchmaster.LOG.debug("Allowed village siege @ {}", attemptedSpawnPos);
            }
        });
    }

    public static void onServerLevelTickEnd(MinecraftServer server, BooleanSupplier haveTime)
    {
        for(ServerLevel level : server.getAllLevels())
        {
            Torchmaster.getRegistryForLevel(level).ifPresent(reg -> reg.onGlobalTick(level));
        }
    }
}
