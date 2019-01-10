package net.xalcon.torchmaster.common.creativetabs;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.xalcon.torchmaster.TorchMasterMod;
import net.xalcon.torchmaster.common.ModBlocks;

public class ItemGroupTorchMaster extends ItemGroup
{
	public static ItemGroupTorchMaster INSTANCE = new ItemGroupTorchMaster();

	public ItemGroupTorchMaster()
	{
		super(TorchMasterMod.MODID);
	}

	@Override
	public ItemStack createIcon()
	{
		return new ItemStack(ModBlocks.getMegaTorch(), 1);
	}
}
