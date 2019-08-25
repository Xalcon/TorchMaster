package net.xalcon.torchmaster.compat;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.util.Tuple;
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
			.map(rl -> new EntityInfoWrapper(rl, EntityList.getClass(rl)))
			.filter(e -> e.getEntityClass() != null) // dont ask me why, but some ResourceLocations return null, i.e. minecraft:lightning_bolt
			.filter(e -> IMob.class.isAssignableFrom(e.getEntityClass()))
			.forEach(e -> event.getRegistry().registerEntity(e.getEntityName()));

	}

	@SubscribeEvent
	public static void registerDreadLampEntities(EntityFilterRegisterEvent.DreadLamp event)
	{
		EntityList.getEntityNameList().stream()
			.map(rl -> new EntityInfoWrapper(rl, EntityList.getClass(rl)))
			.filter(e -> e.getEntityClass() != null)
			.filter(e -> (IAnimals.class.isAssignableFrom(e.getEntityClass()) || EntityCreature.class.isAssignableFrom(e.getEntityClass()))
					&& !IMob.class.isAssignableFrom(e.getEntityClass()))
			.filter(e -> !EntityVillager.class.isAssignableFrom(e.getEntityClass()))
			.forEach(e-> event.getRegistry().registerEntity(e.getEntityName()));
	}
}
