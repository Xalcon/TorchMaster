package net.xalcon.torchmaster.compat;

import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.ForgeRegistries;
import net.xalcon.torchmaster.common.EntityFilterRegistry;

public class VanillaCompat
{
	public static void registerTorchEntities(EntityFilterRegistry registry)
	{
		ForgeRegistries.ENTITY_TYPES.getKeys().stream()
			.map(rl -> new EntityInfoWrapper(rl, ForgeRegistries.ENTITY_TYPES.getValue(rl)))
			.filter(e -> e.getEntityType() != null) // dont ask me why, but some ResourceLocations return null, i.e. minecraft:lightning_bolt
			.filter(e -> !e.getEntityType().getCategory().isFriendly())
			.forEach(e -> registry.registerEntity(e.getEntityName()));

	}

	public static void registerDreadLampEntities(EntityFilterRegistry registry)
	{
		ForgeRegistries.ENTITY_TYPES.getKeys().stream()
			.map(rl -> new EntityInfoWrapper(rl, ForgeRegistries.ENTITY_TYPES.getValue(rl)))
			.filter(e -> e.getEntityType() != null) // dont ask me why, but some ResourceLocations return null, i.e. minecraft:lightning_bolt
			.filter(e -> {
				MobCategory cat = e.getEntityType().getCategory();
				return cat != MobCategory.MISC && cat.isFriendly();
			})
			.forEach(e -> registry.registerEntity(e.getEntityName()));
	}
}
