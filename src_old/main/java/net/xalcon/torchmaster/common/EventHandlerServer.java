package net.xalcon.torchmaster.common;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.xalcon.torchmaster.Torchmaster;
import net.xalcon.torchmaster.common.init.ModCaps;
import net.xalcon.torchmaster.common.logic.CapabilityProviderTorchRegistryContainer;

@Mod.EventBusSubscriber(modid = Torchmaster.MODID)
public class EventHandlerServer
{
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onEntityCheckSpawn(LivingSpawnEvent.CheckSpawn event)
	{
		if(event.getResult() == Event.Result.ALLOW) return;
		if(TorchmasterConfig.MegaTorchAllowVanillaSpawners && event.isSpawner()) return;

		Entity entity = event.getEntity();
		World world = entity.getEntityWorld();

		world.getCapability(ModCaps.TORCH_REGISTRY_CONTAINER).ifPresent(c ->
        {
            if(c.shouldEntityBeBlocked(entity))
                event.setResult(Event.Result.DENY);
        });
	}

    @SubscribeEvent
	public static void onWorldAttachCapabilityEvent(AttachCapabilitiesEvent<World> event)
    {
        event.addCapability(new ResourceLocation(Torchmaster.MODID, "registry"), new CapabilityProviderTorchRegistryContainer());
    }

    @SubscribeEvent
    public static void onGlobalTick(TickEvent.ServerTickEvent event)
    {
        if(event.side == LogicalSide.CLIENT) return;
        if(event.phase == TickEvent.Phase.END)
        {
            for(int dimId : DimensionManager.getIDs())
            {
                World world = DimensionManager.getWorld(dimId);
                if(world == null) return;
                world.getCapability(ModCaps.TORCH_REGISTRY_CONTAINER).ifPresent(c -> c.onGlobalTick(world));
            }
        }
    }
}
