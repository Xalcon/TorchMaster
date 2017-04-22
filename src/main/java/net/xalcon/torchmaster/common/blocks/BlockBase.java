package net.xalcon.torchmaster.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.xalcon.torchmaster.TorchMasterMod;
import net.xalcon.torchmaster.client.IItemRenderRegister;

public class BlockBase extends Block
{
	protected String internalName;

	public BlockBase(Material material, String name)
	{
		super(material);
		this.internalName = name;
		setUnlocalizedName(this.internalName);
		setRegistryName(this.internalName);
		setCreativeTab(CreativeTabs.MISC);
	}

	public void registerItemModels(ItemBlock itemBlock, IItemRenderRegister register)
	{
		//noinspection ConstantConditions
		register.registerItemRenderer(itemBlock, 0, this.getRegistryName(), "inventory");
	}
}

