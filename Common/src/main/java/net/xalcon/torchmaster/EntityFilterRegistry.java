package net.xalcon.torchmaster;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import java.util.HashSet;
import java.util.Set;

public class EntityFilterRegistry
{
	private Set<ResourceLocation> registry = new HashSet<>();

	public boolean containsEntity(ResourceLocation entityName)
	{
		return this.registry.contains(entityName);
	}

	public void registerEntity(ResourceLocation entityName)
	{
		this.registry.add(entityName);
	}

	public void applyListOverrides(String[] overrides)
	{
		for(String override: overrides)
		{
			// minimum len is prefix + valid resource location, i.e. +a:b
			if(override.length() < 4) continue;

			char prefix = override.charAt(0);
			ResourceLocation rl = new ResourceLocation(override.substring(1));

			switch (prefix)
			{
				case '+':
					if(!this.containsEntity(rl))
					{
						if(!Registry.ENTITY_TYPE.containsKey(rl))
						{
							Constants.LOG.warn("  The entity '{}' does not exist, skipping", rl);
							continue;
						}
						this.registerEntity(rl);
						Constants.LOG.info("  Added '{}' to the block list", rl);
					}
					break;
				case '-':
					if(this.registry.removeIf(rrl -> rrl.equals(rl)))
					{
						Constants.LOG.info("  Removed '{}' from the block list", rl);
					}
					break;
				default:
					Constants.LOG.warn("  Invalid block list prefix: '{}', only + and - are valid prefixes", prefix);
					break;
			}
		}
	}

	public ResourceLocation[] getRegisteredEntities()
	{
		return this.registry.toArray(new ResourceLocation[0]);
	}
}
