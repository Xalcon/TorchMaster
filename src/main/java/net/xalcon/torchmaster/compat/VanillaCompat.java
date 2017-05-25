package net.xalcon.torchmaster.compat;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.IAnimals;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Objects;

@Mod.EventBusSubscriber
public class VanillaCompat
{
	@SubscribeEvent
	public static void registerTorchEntities(EntityFilterRegisterEvent.MegaTorch event)
	{
		EntityList.getEntityNameList().stream()
				.map(EntityList.NAME_TO_CLASS::get)
				.filter(Objects::nonNull)
				.filter(IMob.class::isAssignableFrom)
				.forEach(event.getRegistry()::registerEntity);

	}

	@SubscribeEvent
	public static void registerDreadLampEntities(EntityFilterRegisterEvent.DreadLamp event)
	{
		EntityList.getEntityNameList().stream()
				.map(EntityList.NAME_TO_CLASS::get)
				.filter(Objects::nonNull)
				.filter(c -> IAnimals.class.isAssignableFrom(c) && !IMob.class.isAssignableFrom(c))
				.forEach(event.getRegistry()::registerEntity);
	}
}
