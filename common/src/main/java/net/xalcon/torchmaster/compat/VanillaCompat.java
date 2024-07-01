package net.xalcon.torchmaster.compat;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.MobCategory;
import net.xalcon.torchmaster.EntityFilterList;

public class VanillaCompat
{
	public static void registerTorchEntities(EntityFilterList registry)
	{
		// TODO: Fix deprecation warning
		BuiltInRegistries.ENTITY_TYPE.stream()
			.map(entityType -> new EntityInfoWrapper(entityType.builtInRegistryHolder().key().location(), entityType))
			.filter(e -> e.getEntityType() != null) // dont ask me why, but some ResourceLocations return null, i.e. minecraft:lightning_bolt
			.filter(e -> !e.getEntityType().getCategory().isFriendly())
			.forEach(e -> registry.registerEntity(e.getEntityName()));

	}

	public static void registerDreadLampEntities(EntityFilterList registry)
	{
		// TODO: Fix deprecation warning
		BuiltInRegistries.ENTITY_TYPE.stream()
			.map(entityType -> new EntityInfoWrapper(entityType.builtInRegistryHolder().key().location(), entityType))
			.filter(e -> e.getEntityType() != null) // dont ask me why, but some ResourceLocations return null, i.e. minecraft:lightning_bolt
			.filter(e -> {
				MobCategory cat = e.getEntityType().getCategory();
				return cat != MobCategory.MISC && cat.isFriendly();
			})
			.forEach(e -> registry.registerEntity(e.getEntityName()));
	}
}
