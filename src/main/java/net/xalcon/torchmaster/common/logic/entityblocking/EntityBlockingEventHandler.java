package net.xalcon.torchmaster.common.logic.entityblocking;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
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
    public static void onCheckSpawn(LivingSpawnEvent.CheckSpawn event) throws InterruptedException
    {
        boolean log = TorchmasterConfig.GENERAL.logSpawnChecks.get();
        if (log) Torchmaster.Log.debug("CheckSpawn - IsSpawner: {}, Reason: {}, Type: {}", event.isSpawner(), event.getSpawnReason(), event.getEntity().getType().getRegistryName());
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
                if (log) Torchmaster.Log.debug("Blocking spawn of {}", event.getEntity().getType().getRegistryName());
                //event.getEntity().addTag("torchmaster_removed_spawn");
            }
            else
            {
                if (log) Torchmaster.Log.debug("Allowed spawn of {}", event.getEntity().getType().getRegistryName());
            }
        });
    }

    /*@SubscribeEvent
    public static void onEntityJoinWorldEvent(EntityJoinWorldEvent event)
    {
        event.getWorld().getWorld().getProfiler().startSection("torchmaster_ejw");
        if(event.getEntity().getTags().contains("torchmaster_removed_spawn"))
        {
            event.setCanceled(true);
            event.setResult(Event.Result.DENY);
        }
        event.getWorld().getWorld().getProfiler().endSection();
    }*/

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
                level.getProfiler().push("torchmaster_" + level.dimension().getRegistryName());
                level.getCapability(ModCaps.TEB_REGISTRY).ifPresent(reg -> reg.onGlobalTick(level));
                level.getProfiler().pop();
            }
        }
    }
}
