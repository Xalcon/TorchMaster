package net.xalcon.torchmaster.common.logic.entityblocking;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.xalcon.torchmaster.Torchmaster;
import net.xalcon.torchmaster.TorchmasterConfig;
import net.xalcon.torchmaster.common.ModCaps;

@Mod.EventBusSubscriber(modid = Torchmaster.MODID)
public class EntityBlockingEventHandler
{
    @SubscribeEvent
    public static void onCheckSpawn(LivingSpawnEvent.CheckSpawn event) throws InterruptedException
    {
        event.getWorld().getWorld().getProfiler().startSection("torchmaster_checkspawn");
        if(event.getResult() == Event.Result.ALLOW) return;
        if(TorchmasterConfig.GENERAL.blockOnlyNaturalSpawns.get() && event.isSpawner()) return;

        Entity entity = event.getEntity();
        World world = entity.getEntityWorld();

        world.getCapability(ModCaps.TEB_REGISTRY).ifPresent(reg ->
        {
            if(reg.shouldBlockEntity(entity))
            {
                event.setResult(Event.Result.DENY);
                event.getEntity().addTag("torchmaster_removed_spawn");
            }
        });
        event.getWorld().getWorld().getProfiler().endSection();
    }

    @SubscribeEvent
    public static void onEntityJoinWorldEvent(EntityJoinWorldEvent event)
    {
        event.getWorld().getWorld().getProfiler().startSection("torchmaster_ejw");
        if(event.getEntity().getTags().contains("torchmaster_removed_spawn"))
        {
            event.setCanceled(true);
            event.setResult(Event.Result.DENY);
        }
        event.getWorld().getWorld().getProfiler().endSection();
    }

    @SubscribeEvent
    public static void onWorldAttachCapabilityEvent(AttachCapabilitiesEvent<World> event)
    {
        event.addCapability(new ResourceLocation(Torchmaster.MODID, "registry"), new LightsRegistryCapability());
    }

    @SubscribeEvent
    public static void onGlobalTick(TickEvent.ServerTickEvent event)
    {
        if(event.side == LogicalSide.CLIENT) return;
        if(event.phase == TickEvent.Phase.END)
        {
            for(ServerWorld world : Torchmaster.server.getWorlds())
            {
                world.getProfiler().startSection("torchmaster_dim" + world.dimension.getType().getId());
                world.getCapability(ModCaps.TEB_REGISTRY).ifPresent(reg -> reg.onGlobalTick(world));
                world.getProfiler().endSection();
            }
        }
    }
}
