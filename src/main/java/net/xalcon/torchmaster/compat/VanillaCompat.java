package net.xalcon.torchmaster.compat;

import net.minecraft.entity.EntityClassification;
import net.minecraftforge.registries.ForgeRegistries;
import net.xalcon.torchmaster.common.EntityFilterRegistry;

public class VanillaCompat
{
	public static void registerTorchEntities(EntityFilterRegistry registry)
	{
		ForgeRegistries.ENTITIES.getKeys().stream()
			.map(rl -> new EntityInfoWrapper(rl, ForgeRegistries.ENTITIES.getValue(rl)))
			.filter(e -> e.getEntityType() != null) // dont ask me why, but some ResourceLocations return null, i.e. minecraft:lightning_bolt
			.filter(e -> !e.getEntityType().getClassification().getPeacefulCreature())
			.forEach(e -> registry.registerEntity(e.getEntityName()));

	}

	public static void registerDreadLampEntities(EntityFilterRegistry registry)
	{
		ForgeRegistries.ENTITIES.getKeys().stream()
			.map(rl -> new EntityInfoWrapper(rl, ForgeRegistries.ENTITIES.getValue(rl)))
			.filter(e -> e.getEntityType() != null) // dont ask me why, but some ResourceLocations return null, i.e. minecraft:lightning_bolt
			.filter(e -> {
				EntityClassification cls = e.getEntityType().getClassification();
				return cls != EntityClassification.MISC && cls.getPeacefulCreature();
			})
			.forEach(e -> registry.registerEntity(e.getEntityName()));
	}
}
