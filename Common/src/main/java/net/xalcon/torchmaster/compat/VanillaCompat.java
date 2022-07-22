package net.xalcon.torchmaster.compat;

import net.minecraft.core.Registry;
import net.minecraft.world.entity.MobCategory;
import net.xalcon.torchmaster.EntityFilterRegistry;

public class VanillaCompat
{
	public static void registerTorchEntities(EntityFilterRegistry registry)
	{
		Registry.ENTITY_TYPE.stream()
			.map(entityType -> new EntityInfoWrapper(entityType.builtInRegistryHolder().key().location(), entityType))
			.filter(e -> e.getEntityType() != null) // dont ask me why, but some ResourceLocations return null, i.e. minecraft:lightning_bolt
			.filter(e -> !e.getEntityType().getCategory().isFriendly())
			.forEach(e -> registry.registerEntity(e.getEntityName()));

	}

	public static void registerDreadLampEntities(EntityFilterRegistry registry)
	{
		Registry.ENTITY_TYPE.stream()
			.map(entityType -> new EntityInfoWrapper(entityType.builtInRegistryHolder().key().location(), entityType))
			.filter(e -> e.getEntityType() != null) // dont ask me why, but some ResourceLocations return null, i.e. minecraft:lightning_bolt
			.filter(e -> {
				MobCategory cat = e.getEntityType().getCategory();
				return cat != MobCategory.MISC && cat.isFriendly();
			})
			.forEach(e -> registry.registerEntity(e.getEntityName()));
	}
}
