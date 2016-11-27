package net.xalcon.torchmaster.common;

import net.minecraft.item.Item;

public abstract class Proxy
{
	public abstract void RegisterItemRenderer(Item item, int meta, String id);
}
