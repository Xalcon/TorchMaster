package net.xalcon.torchmaster.common.utils;

import net.minecraft.item.ItemStack;

public class ItemstackUtils
{
	public static boolean isEmpty(ItemStack itemstack)
	{
		return itemstack == null || itemstack.stackSize == 0;
	}
}
