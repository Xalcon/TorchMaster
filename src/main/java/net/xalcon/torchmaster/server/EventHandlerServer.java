package net.xalcon.torchmaster.server;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.xalcon.torchmaster.TorchMasterMod;
import net.xalcon.torchmaster.common.utils.NbtUtils;

public class EventHandlerServer
{
	@SubscribeEvent
	public void onEntityCheckSpawn(LivingSpawnEvent.CheckSpawn event)
	{
		if(event.getResult() == Event.Result.ALLOW) return;
		if(event.getEntityLiving() instanceof IMob)
		{
			if(TorchRegistry.getMegaTorchRegistry().isInRangeOfTorch(event.getWorld(), event.getEntity().getPosition()))
			{
				if(!TorchMasterMod.Configuration.isVanillaSpawnerEnabled() || !event.getEntity().getTags().contains("IsSpawnerMob"))
					event.setResult(Event.Result.DENY);
			}
		}
		else if(event.getEntityLiving() instanceof IAnimals)
		{
			if(TorchRegistry.getDreadLampRegistry().isInRangeOfTorch(event.getWorld(), event.getEntity().getPosition()))
			{
				if(!TorchMasterMod.Configuration.isVanillaSpawnerEnabled() || !event.getEntity().getTags().contains("IsSpawnerMob"))
					event.setResult(Event.Result.DENY);
			}
		}
	}
}
