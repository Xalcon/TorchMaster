package net.xalcon.torchmaster.compat;

import net.minecraftforge.fml.common.eventhandler.Event;
import net.xalcon.torchmaster.common.EntityFilterRegistry;

public abstract class EntityFilterRegisterEvent extends Event
{
	private EntityFilterRegistry registry;

	public EntityFilterRegisterEvent(EntityFilterRegistry registry)
	{
		this.registry = registry;
	}

	public EntityFilterRegistry getRegistry() { return this.registry; }

	public static class MegaTorch extends EntityFilterRegisterEvent
	{
		public MegaTorch(EntityFilterRegistry registry)
		{
			super(registry);
		}
	}

	public static class DreadLamp extends EntityFilterRegisterEvent
	{
		public DreadLamp(EntityFilterRegistry registry)
		{
			super(registry);
		}
	}
}
