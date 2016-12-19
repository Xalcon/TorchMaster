package net.xalcon.torchmaster.common.utils;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

public class BlockUtils
{
	public static IBlockState getBlockStateFromItemStack(ItemStack itemStack)
	{
		Block block = Block.getBlockFromItem(itemStack.getItem());
		if (block == null) return null;
		return block.getStateFromMeta(itemStack.getMetadata());
	}

	// Algorithm by mako @ http://stackoverflow.com/a/14010215
	public static int[] createSpiralMap(int radius)
	{
		int x = 0, y = 0, layer = 1, arm = 0;
		int[] array = new int[((radius + radius + 1) * (radius + radius + 1)) * 2];
		for (int i = 0; i < array.length; i += 2)
		{
			switch(arm)
			{
				case 0: ++x; if(x == layer) ++arm; break;
				case 1: ++y; if(y == layer) ++arm; break;
				case 2: --x; if(-x == layer) ++arm; break;
				case 3: --y; if(-y == layer) {++layer; arm = 0;} break;
			}
			array[i] = x;
			array[i+1] = y;
		}
		return array;
	}
}
