package net.xalcon.torchmaster.common;

import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.world.WorldEvent;
import net.xalcon.torchmaster.common.utils.NbtUtils;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class EntityFilterRegistry
{
	private Set<Class<?>> registry = new HashSet<>();

	public boolean containsEntity(Entity entity)
	{
		return containsEntity(entity.getClass());
	}

	public boolean containsEntity(Class<?> entityClass)
	{
		return this.registry.contains(entityClass);
	}

	public void registerEntity(Entity entity)
	{
		this.registerEntity(entity.getClass());
	}

	public void registerEntity(Class<?> entityClass)
	{
		this.registry.add(entityClass);
	}
}
