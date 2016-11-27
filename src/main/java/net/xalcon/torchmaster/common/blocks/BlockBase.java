package net.xalcon.torchmaster.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.xalcon.torchmaster.TorchMasterMod;

public class BlockBase extends Block
{
	protected String internalName;

	public BlockBase(Material material, String name)
	{
		super(material);
		this.internalName = name;
		setUnlocalizedName(this.internalName);
		setRegistryName(this.internalName);
	}

	public void RegisterItemModel(ItemBlock itemBlock)
	{
		TorchMasterMod.Proxy.RegisterItemRenderer(itemBlock, 0, this.internalName);
	}
}

