package net.xalcon.torchmaster.common;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.util.ResourceLocation;
import net.xalcon.torchmaster.TorchMasterMod;

import java.util.Collections;
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
						if(EntityList.getClass(rl) == null)
						{
							TorchMasterMod.Log.warn("  The entity '{}' does not exist, skipping", rl);
							continue;
						}
						this.registerEntity(rl);
						TorchMasterMod.Log.info("  Added '{}' to the block list", rl);
					}
					break;
				case '-':
					if(this.registry.removeIf(rrl -> rrl.equals(rl)))
					{
						TorchMasterMod.Log.info("  Removed '{}' from the block list", rl);
					}
					break;
				default:
					TorchMasterMod.Log.warn("  Invalid block list prefix: '{}', only + and - are valid prefixes");
					break;
			}
		}
	}
}
