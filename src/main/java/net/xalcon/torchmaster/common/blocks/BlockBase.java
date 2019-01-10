package net.xalcon.torchmaster.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.xalcon.torchmaster.common.items.ItemBlockTooltipInfo;

public abstract class BlockBase extends Block
{
	public BlockBase(String internalName, Builder builder)
	{
		super(builder);
		setRegistryName(internalName);
	}

	@OnlyIn(Dist.CLIENT)
	public void registerItemModels(Item item)
	{
		// TODO: Item model registration
		//ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(this.getRegistryName(), "inventory"));
	}

	public Item createItemBlock()
	{
		return new ItemBlockTooltipInfo(this).setRegistryName(this.getRegistryName());
	}
}

