package net.xalcon.torchmaster.common.itemgroups;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.xalcon.torchmaster.Torchmaster;
import net.xalcon.torchmaster.common.init.ModBlocks;

public class ItemGroupTorchMaster extends ItemGroup
{
	public static ItemGroupTorchMaster INSTANCE = new ItemGroupTorchMaster();

	public ItemGroupTorchMaster()
	{
		super(Torchmaster.MODID);
	}

	@Override
	public ItemStack createIcon()
	{
		return new ItemStack(ModBlocks.MegaTorch, 1);
	}
}
