package net.xalcon.torchmaster.common;

import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.xalcon.torchmaster.TorchMasterMod;

public class EventHandlerServer
{
	@SubscribeEvent
	public void onEntityCheckSpawn(LivingSpawnEvent.CheckSpawn event)
	{
		if(event.getResult() == Event.Result.ALLOW) return;
		if(TorchMasterMod.MegaTorchFilterRegistry.containsEntity(event.getEntity()))
		{
			if(TorchRegistry.getMegaTorchRegistry().isInRangeOfTorch(event.getWorld(), event.getEntity().getPosition()))
			{
				if(!TorchMasterMod.isNotInWorldTick || !TorchMasterMod.ConfigHandler.isVanillaSpawnerEnabled())
					event.setResult(Event.Result.DENY);
			}
		}
		else if(TorchMasterMod.DreadLampFilterRegistry.containsEntity(event.getEntity()))
		{
			if(TorchRegistry.getDreadLampRegistry().isInRangeOfTorch(event.getWorld(), event.getEntity().getPosition()))
			{
				if(!TorchMasterMod.isNotInWorldTick || !TorchMasterMod.ConfigHandler.isVanillaSpawnerEnabled())
					event.setResult(Event.Result.DENY);
			}
		}
	}
}
