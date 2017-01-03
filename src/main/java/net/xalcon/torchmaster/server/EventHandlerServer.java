package net.xalcon.torchmaster.server;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.xalcon.torchmaster.common.utils.NbtUtils;

public class EventHandlerServer
{
	@SubscribeEvent
	public void onEntityCheckSpawn(LivingSpawnEvent.CheckSpawn event)
	{
		if(event.getResult() == Event.Result.ALLOW) return;
		if(event.getEntityLiving().isCreatureType(EnumCreatureType.MONSTER, false)
				&& TorchRegistry.INSTANCE.isInRangeOfTorch(event.getWorld(), event.getEntity().getPosition()))
		{
			if(!event.getEntity().getTags().contains("IsSpawnerMob"))
				event.setResult(Event.Result.DENY);
		}
	}
}
