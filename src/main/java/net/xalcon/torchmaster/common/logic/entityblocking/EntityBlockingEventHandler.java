package net.xalcon.torchmaster.common.logic.entityblocking;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.village.VillageSiegeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.xalcon.torchmaster.Torchmaster;
import net.xalcon.torchmaster.TorchmasterConfig;
import net.xalcon.torchmaster.common.ModCaps;

@Mod.EventBusSubscriber(modid = Torchmaster.MODID)
public class EntityBlockingEventHandler
{
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onCheckSpawn(LivingSpawnEvent.CheckSpawn event)
    {
        boolean log = TorchmasterConfig.GENERAL.logSpawnChecks.get();
        if (log) Torchmaster.Log.debug("CheckSpawn - IsSpawner: {}, Reason: {}, Type: {}, Pos: {}/{}/{}", event.isSpawner(), event.getSpawnReason(), EntityType.getKey(event.getEntity().getType()), event.getX(), event.getY(), event.getZ());
        if(!TorchmasterConfig.GENERAL.aggressiveSpawnChecks.get() && event.getResult() == Event.Result.ALLOW) return;
        if(TorchmasterConfig.GENERAL.blockOnlyNaturalSpawns.get() && event.isSpawner()) return;

        var entity = event.getEntity();
        var world = entity.getCommandSenderWorld();
        
        var pos = new BlockPos(event.getX(), event.getY(), event.getZ());

        world.getCapability(ModCaps.TEB_REGISTRY).ifPresent(reg ->
        {
            if(reg.shouldBlockEntity(entity, pos))
            {
                event.setResult(Event.Result.DENY);
                if (log) Torchmaster.Log.debug("Blocking spawn of {}", EntityType.getKey(event.getEntity().getType()));
                //event.getEntity().addTag("torchmaster_removed_spawn");
            }
            else
            {
                if (log) Torchmaster.Log.debug("Allowed spawn of {}", EntityType.getKey(event.getEntity().getType()));
            }
        });
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onVillageSiegeEvent(VillageSiegeEvent event)
    {
        if(!TorchmasterConfig.GENERAL.blockVillageSieges.get()) return;
        boolean log = TorchmasterConfig.GENERAL.logSpawnChecks.get();
        if (log) Torchmaster.Log.debug("VillageSiegeEvent - Pos: {}", event.getAttemptedSpawnPos());
        if(!TorchmasterConfig.GENERAL.aggressiveSpawnChecks.get() && event.getResult() == Event.Result.ALLOW) return;

        var vec = event.getAttemptedSpawnPos();
        var pos = new BlockPos(vec.x, vec.y, vec.z);
        var level = event.getLevel();

        level.getCapability(ModCaps.TEB_REGISTRY).ifPresent(reg ->
        {
            if(reg.shouldBlockVillageSiege(pos))
            {
                event.setResult(Event.Result.DENY);
                if(event.isCancelable())
                    event.setCanceled(true);
                if (log) Torchmaster.Log.debug("Blocking village siege @ {}", pos);
            }
            else
            {
                if (log) Torchmaster.Log.debug("Allowed village siege @ {}", pos);
            }
        });

    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onDoSpecialSpawn(LivingSpawnEvent.SpecialSpawn event)
    {
        boolean log = TorchmasterConfig.GENERAL.logSpawnChecks.get();
        if (log) Torchmaster.Log.debug("DoSpecialSpawn - Reason: {}, Type: {}, Pos: {}/{}/{}", event.getSpawnReason(), EntityType.getKey(event.getEntity().getType()), event.getX(), event.getY(), event.getZ());
        // Respect other mods decisions to allow a spawn if enabled in config
        if(!TorchmasterConfig.GENERAL.aggressiveSpawnChecks.get() && event.getResult() == Event.Result.ALLOW) return;
        // check if non-natural spawns should be blocked
        if(TorchmasterConfig.GENERAL.blockOnlyNaturalSpawns.get() && event.getSpawnReason() == MobSpawnType.SPAWNER) return;
        if(event.getSpawnReason() != MobSpawnType.NATURAL && event.getSpawnReason() != MobSpawnType.STRUCTURE) return;

        var entity = event.getEntity();
        var world = entity.getCommandSenderWorld();
        var pos = new BlockPos(event.getX(), event.getY(), event.getZ());

        world.getCapability(ModCaps.TEB_REGISTRY).ifPresent(reg ->
        {
            if(reg.shouldBlockEntity(entity, pos))
            {
                event.setResult(Event.Result.DENY);
                if (log) Torchmaster.Log.debug("Blocking spawn of {}", EntityType.getKey(event.getEntity().getType()));
            }
            else
            {
                if (log) Torchmaster.Log.debug("Allowed spawn of {}", EntityType.getKey(event.getEntity().getType()));
            }
        });
    }

    @SubscribeEvent
    public static void onWorldAttachCapabilityEvent(AttachCapabilitiesEvent<Level> event)
    {
        event.addCapability(new ResourceLocation(Torchmaster.MODID, "registry"), new LightsRegistryCapability());
    }

    @SubscribeEvent
    public static void onGlobalTick(TickEvent.ServerTickEvent event)
    {
        if(event.side == LogicalSide.CLIENT) return;
        if(event.phase == TickEvent.Phase.END)
        {
            if(Torchmaster.server == null) return;

            for(ServerLevel level : Torchmaster.server.getAllLevels())
            {
                level.getProfiler().push("torchmaster_" + level.dimension().registry());
                level.getCapability(ModCaps.TEB_REGISTRY).ifPresent(reg -> reg.onGlobalTick(level));
                level.getProfiler().pop();
            }
        }
    }
}
