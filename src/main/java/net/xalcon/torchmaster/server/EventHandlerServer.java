package net.xalcon.torchmaster.server;

import net.minecraft.entity.EnumCreatureType;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventHandlerServer
{
	@SubscribeEvent
	public void onEntityCheckSpawn(LivingSpawnEvent.CheckSpawn event)
	{
		if(event.getResult() == Event.Result.ALLOW) return;
		if(event.getEntityLiving().isCreatureType(EnumCreatureType.MONSTER, false)
				&& TorchRegistry.INSTANCE.isInRangeOfTorch(event.getWorld(), event.getEntity().getPosition()))
		{
			event.setResult(Event.Result.DENY);
		}
	}
}
