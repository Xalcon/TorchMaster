package net.xalcon.torchmaster.common;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ModRecipes
{
	public static void init()
	{
		GameRegistry.addRecipe(new ShapedOreRecipe(
				new ItemStack(ModBlocks.MegaTorch),
				"TTT", "DLD", "GLG",
				'T', Blocks.TORCH,
				'D', "gemDiamond",
				'G', "blockGold", 'L', "logWood"));

		GameRegistry.addRecipe(new ShapedOreRecipe(
				new ItemStack(ModBlocks.DreadLamp),
				"OOO", "PGP", "OIO",
				'O', Blocks.OBSIDIAN,
				'P', "paneGlass",
				'G', "glowstone",
				'I', "dyeBlack"));

		GameRegistry.addRecipe(new ShapedOreRecipe(
				new ItemStack(ModBlocks.TerrainLighter),
				"STS", "LCL", "sss",
				'S', "stickWood",
				'T', Blocks.TORCH,
				'C', Blocks.CHEST,
				'L', "logWood",
				's', "stone"
		));
	}
}
