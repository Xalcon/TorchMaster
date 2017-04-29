package net.xalcon.torchmaster.common.items;

import net.minecraft.block.Block;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.xalcon.torchmaster.TorchMasterMod;
import net.xalcon.torchmaster.common.ModBlocks;
import net.xalcon.torchmaster.common.blocks.BlockMegaTorch;

public class ItemBlockMegaTorch extends ItemBlock
{
	public ItemBlockMegaTorch(Block block)
	{
		super(block);

		this.setHasSubtypes(true);
	}

	@Override
	public int getMetadata(int damage)
	{
		// vanilla item blocks return "0" instead of the damage value
		return damage;
	}

	public String getUnlocalizedName(ItemStack stack)
	{
		return super.getUnlocalizedName() + "." + (ModBlocks.MegaTorch.getStateFromMeta(stack.getMetadata()).getValue(BlockMegaTorch.BURNING) ? "lit" : "unlit");
	}
}
