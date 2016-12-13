package net.xalcon.torchmaster.common.utils;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

public class BlockUtils
{
	public static IBlockState getBlockStateFromItemStack(ItemStack itemStack)
	{
		Block block = Block.getBlockFromItem(itemStack.getItem());
		if(block == null) return null;
		return block.getStateFromMeta(itemStack.getMetadata());
	}
}
