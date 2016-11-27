package net.xalcon.torchmaster.common;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

/**
 * Created by xalcon on 27.11.2016.
 */
public class ModRecipes
{
	public static void init()
	{
		GameRegistry.addRecipe(new ShapedOreRecipe(
				new ItemStack(ModBlocks.MegaTorch),
				"TTT", "DLD", "GLG",
				'T', Blocks.TORCH,
				'D', Items.DIAMOND,
				'G', Blocks.GOLD_BLOCK, 'L', "logWood"));
	}
}
