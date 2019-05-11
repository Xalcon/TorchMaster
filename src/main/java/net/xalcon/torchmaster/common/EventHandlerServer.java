package net.xalcon.torchmaster.common;

import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.xalcon.torchmaster.TorchMasterMod;
import net.xalcon.torchmaster.common.logic.CapabilityProviderTorchRegistryContainer;
import net.xalcon.torchmaster.common.logic.ITorchRegistryContainer;
import net.xalcon.torchmaster.common.logic.TorchRegistryContainer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EventHandlerServer
{
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onEntityCheckSpawn(LivingSpawnEvent.CheckSpawn event)
	{
		if(event.getResult() == Event.Result.ALLOW && !TorchmasterConfig.slighlyMoreAggressiveBlocking) return;
		if(TorchmasterConfig.MegaTorchAllowVanillaSpawners && event.isSpawner()) return;

		Entity entity = event.getEntity();
		World world = entity.getEntityWorld();

		ITorchRegistryContainer container = world.getCapability(ModCaps.TORCH_REGISTRY_CONTAINER, null);
		if(container != null && container.shouldEntityBeBlocked(entity))
            event.setResult(Event.Result.DENY);
	}

    @SubscribeEvent
	public void onWorldAttachCapabilityEvent(AttachCapabilitiesEvent<World> event)
    {
        event.addCapability(new ResourceLocation(TorchMasterMod.MODID, "registry"), new CapabilityProviderTorchRegistryContainer());
    }

    @SubscribeEvent
    public void onGlobalTick(TickEvent.ServerTickEvent event)
    {
        if(event.side == Side.CLIENT) return;
        if(event.phase == TickEvent.Phase.END)
        {
            for(int dimId : DimensionManager.getIDs())
            {
                World world = DimensionManager.getWorld(dimId);
                if(world == null) return;
                ITorchRegistryContainer container = world.getCapability(ModCaps.TORCH_REGISTRY_CONTAINER, null);
                if(container != null)
                    container.onGlobalTick(world);
            }
        }
    }
}
