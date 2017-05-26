package net.xalcon.torchmaster.common.creativetabs;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmorStand;
import net.minecraft.item.ItemStack;
import net.xalcon.torchmaster.TorchMasterMod;
import net.xalcon.torchmaster.common.ModBlocks;

public class CreativeTabTorchMaster extends CreativeTabs
{
	public static CreativeTabTorchMaster INSTANCE = new CreativeTabTorchMaster();

	public CreativeTabTorchMaster()
	{
		super(TorchMasterMod.MODID);
	}

	@Override
	public ItemStack getTabIconItem()
	{
		return new ItemStack(ModBlocks.MegaTorch, 1);
	}
}
