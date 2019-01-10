package net.xalcon.torchmaster.compat;

import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.IAnimal;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber
public class VanillaCompat
{
	@SubscribeEvent
	public static void registerTorchEntities(EntityFilterRegisterEvent.MegaTorch event)
	{
		ForgeRegistries.ENTITIES.getEntries().stream()
				.map(e -> e.getValue().getEntityClass())
				.filter(IMob.class::isAssignableFrom)
				.forEach(event.getRegistry()::registerEntity);
	}

	@SubscribeEvent
	public static void registerDreadLampEntities(EntityFilterRegisterEvent.DreadLamp event)
	{
		ForgeRegistries.ENTITIES.getEntries().stream()
				.map(e -> e.getValue().getEntityClass())
				.filter(c -> IAnimal.class.isAssignableFrom(c) && !IMob.class.isAssignableFrom(c))
				.forEach(event.getRegistry()::registerEntity);
	}
}
