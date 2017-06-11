package net.xalcon.torchmaster.common;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreIngredient;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.xalcon.torchmaster.TorchMasterMod;
import net.xalcon.torchmaster.common.utils.RecipeUtils;

public class ModRecipes
{
	public static void init()
	{
        /*RecipeUtils.add(new ResourceLocation(TorchMasterMod.MODID, "mega_torch0"), new ItemStack(ModBlocks.MegaTorch),
                new String[] { "TTT", "DLD", "GLG" },
                new Tuple<>('T', Ingredient.func_193369_a(new ItemStack(Blocks.TORCH))),
                new Tuple<>('D', new OreIngredient("gemDiamond")),
                new Tuple<>('L', new OreIngredient("logWood")),
                new Tuple<>('G', new OreIngredient("blockGold"))
        );*/

		/*RecipeUtils.add(new ResourceLocation(TorchMasterMod.MODID, "dread_lamp0"), new ItemStack(ModBlocks.DreadLamp),
				new String[] { "OOO", "PGP", "OBO" },
				new Tuple<>('O', Ingredient.func_193369_a(new ItemStack(Blocks.OBSIDIAN))),
				new Tuple<>('P', new OreIngredient("paneGlass")),
				new Tuple<>('G', Ingredient.func_193369_a(new ItemStack(Blocks.GLOWSTONE))),
				new Tuple<>('B', new OreIngredient("dyeBlack"))
				);*/

        /*RecipeUtils.add(new ResourceLocation(TorchMasterMod.MODID, "terrain_lighter0"), new ItemStack(ModBlocks.TerrainLighter),
                new String[] { "STS", "LCL", "sss" },
                new Tuple<>('S', new OreIngredient("stickWood")),
                new Tuple<>('T', Ingredient.func_193369_a(new ItemStack(Blocks.TORCH))),
                new Tuple<>('C', Ingredient.func_193369_a(new ItemStack(Blocks.CHEST))),
                new Tuple<>('L', new OreIngredient("logWood")),
                new Tuple<>('s', new OreIngredient("stone"))
        );*/

        RecipeUtils.add(new ResourceLocation(TorchMasterMod.MODID, "mega_torch0"), new ItemStack(ModBlocks.MegaTorch),
                new String[] { "TTT", "DLD", "GLG" },
                new Tuple<>('T', Ingredient.func_193369_a(new ItemStack(Blocks.TORCH))),
                new Tuple<>('D', Ingredient.func_193369_a(new ItemStack(Items.DIAMOND))),
                new Tuple<>('L', Ingredient.func_193369_a(
                        new ItemStack(Blocks.LOG, 1, 0),
                        new ItemStack(Blocks.LOG, 1, 1),
                        new ItemStack(Blocks.LOG, 1, 2),
                        new ItemStack(Blocks.LOG, 1, 3),
                        new ItemStack(Blocks.LOG2, 1, 0),
                        new ItemStack(Blocks.LOG2, 1, 1))),
                new Tuple<>('G', Ingredient.func_193369_a(new ItemStack(Blocks.GOLD_BLOCK)))
        );

        RecipeUtils.add(new ResourceLocation(TorchMasterMod.MODID, "dread_lamp0"), new ItemStack(ModBlocks.DreadLamp),
                new String[] { "OOO", "PGP", "OBO" },
                new Tuple<>('O', Ingredient.func_193369_a(new ItemStack(Blocks.OBSIDIAN))),
                new Tuple<>('P', Ingredient.func_193369_a(new ItemStack(Blocks.GLASS_PANE))),
                new Tuple<>('G', Ingredient.func_193369_a(new ItemStack(Blocks.GLOWSTONE))),
                new Tuple<>('B', Ingredient.func_193369_a(new ItemStack(Items.DYE, 1, 0)))
        );

        RecipeUtils.add(new ResourceLocation(TorchMasterMod.MODID, "terrain_lighter0"), new ItemStack(ModBlocks.TerrainLighter),
                new String[] { "STS", "LCL", "sss" },
                new Tuple<>('S', Ingredient.func_193369_a(new ItemStack(Items.STICK))),
                new Tuple<>('T', Ingredient.func_193369_a(new ItemStack(Blocks.TORCH))),
                new Tuple<>('C', Ingredient.func_193369_a(new ItemStack(Blocks.CHEST))),
                new Tuple<>('L', Ingredient.func_193369_a(
                        new ItemStack(Blocks.LOG, 1, 0),
                        new ItemStack(Blocks.LOG, 1, 1),
                        new ItemStack(Blocks.LOG, 1, 2),
                        new ItemStack(Blocks.LOG, 1, 3),
                        new ItemStack(Blocks.LOG2, 1, 0),
                        new ItemStack(Blocks.LOG2, 1, 1))),
                new Tuple<>('s', Ingredient.func_193369_a(new ItemStack(Blocks.STONE)))
        );
	}
}
