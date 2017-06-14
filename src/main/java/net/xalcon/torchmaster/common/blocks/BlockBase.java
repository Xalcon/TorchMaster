package net.xalcon.torchmaster.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.xalcon.torchmaster.TorchMasterMod;
import net.xalcon.torchmaster.client.IItemRenderRegister;
import net.xalcon.torchmaster.common.creativetabs.CreativeTabTorchMaster;

public class BlockBase extends Block
{
	protected final String internalName;

	public BlockBase(Material material, String name)
	{
		super(material);
		this.internalName = name;
		setUnlocalizedName(TorchMasterMod.MODID + "." + this.internalName);
		setRegistryName(this.internalName);
		setCreativeTab(CreativeTabTorchMaster.INSTANCE);
	}

	public void registerItemModels(ItemBlock itemBlock, IItemRenderRegister register)
	{
		//noinspection ConstantConditions
		register.registerItemRenderer(itemBlock, 0, this.getRegistryName(), "inventory");
	}

	public ItemBlock createItemBlock()
	{
		return (ItemBlock) new ItemBlock(this).setRegistryName(this.getRegistryName());
	}
}

