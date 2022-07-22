package net.xalcon.torchmaster.common;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.village.VillageSiegeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.xalcon.torchmaster.Constants;
import net.xalcon.torchmaster.Torchmaster;
import net.xalcon.torchmaster.TorchmasterConfig;
import net.xalcon.torchmaster.TorchmasterForge;
import net.xalcon.torchmaster.common.LightsRegistryCapability;
import net.xalcon.torchmaster.common.ModCaps;
import net.xalcon.torchmaster.events.EventResult;
import net.xalcon.torchmaster.events.EventResultContainer;
import net.xalcon.torchmaster.utils.EventUtils;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public class EntityBlockingEventHandler
{
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onCheckSpawn(LivingSpawnEvent.CheckSpawn event)
    {
        var container = EventUtils.asContainer(event);
        var reason = event.getSpawnReason();
        var entity = event.getEntity();
        var pos = new Vec3(event.getX(), event.getY(), event.getZ());
        Torchmaster.EventHandler.onCheckSpawn(reason, entity, pos, container);

        EventUtils.setEventResult(event, container);
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onVillageSiegeEvent(VillageSiegeEvent event)
    {
        var container = EventUtils.asContainer(event);
        Torchmaster.EventHandler.onVillageSiege(event.getLevel(), event.getAttemptedSpawnPos(), container);
        EventUtils.setEventResult(event, container);
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onDoSpecialSpawn(LivingSpawnEvent.SpecialSpawn event)
    {
        var container = EventUtils.asContainer(event);
        var reason = event.getSpawnReason();
        var entity = event.getEntity();
        var pos = new Vec3(event.getX(), event.getY(), event.getZ());
        Torchmaster.EventHandler.onDoSpecialSpawn(reason, entity, pos, container);

        EventUtils.setEventResult(event, container);
    }

    @SubscribeEvent
    public static void onWorldAttachCapabilityEvent(AttachCapabilitiesEvent<Level> event)
    {
        event.addCapability(new ResourceLocation(Constants.MOD_ID, "registry"), new LightsRegistryCapability());
    }
}
