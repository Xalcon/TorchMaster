package net.xalcon.torchmaster;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EntityFilterList
{
	private final ResourceLocation filterListId;
	private final Set<ResourceLocation> list = new HashSet<>();

    public EntityFilterList(ResourceLocation identifier)
    {
        this.filterListId = identifier;
    }

    public boolean containsEntity(ResourceLocation entityName)
	{
		return this.list.contains(entityName);
	}

	public void registerEntity(ResourceLocation entityName)
	{
		this.list.add(entityName);
	}

	public void applyListOverrides(List<String> overrides)
	{
		for(String override: overrides)
		{
			// minimum len is prefix + valid resource location, i.e. +a:b
			if(override.length() < 4)
			{
				Torchmaster.LOG.warn("[{}] Invalid filter definition '{}'", filterListId, override);
				continue;
			}

			char prefix = override.charAt(0);
			var rl = ResourceLocation.parse(override.substring(1));

			switch (prefix)
			{
				case '+':
					if(!this.containsEntity(rl))
					{
						if(!BuiltInRegistries.ENTITY_TYPE.containsKey(rl))
						{
							Torchmaster.LOG.warn("[{}] The entity '{}' does not exist, skipping", filterListId, rl);
							continue;
						}
						this.registerEntity(rl);
						Torchmaster.LOG.info("[{}] Added '{}' to the block list", filterListId, rl);
					}
					break;
				case '-':
					if(this.list.removeIf(rrl -> rrl.equals(rl)))
					{
						Torchmaster.LOG.info("[{}] Removed '{}' from the block list", filterListId, rl);
					}
					break;
				default:
					Torchmaster.LOG.warn("[{}] Invalid block list prefix: '{}', only + and - are valid prefixes", filterListId, prefix);
					break;
			}
		}
	}

	public ResourceLocation[] getEntities()
	{
		return this.list.toArray(new ResourceLocation[0]);
	}

	public void clear()
	{
		list.clear();
	}
}
