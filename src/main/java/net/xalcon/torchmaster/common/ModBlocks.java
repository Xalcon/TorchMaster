package net.xalcon.torchmaster.common;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.xalcon.torchmaster.common.blocks.BlockBase;
import net.xalcon.torchmaster.common.blocks.BlockMegaTorch;
import net.xalcon.torchmaster.common.blocks.BlockTerrainLighter;

public class ModBlocks
{
	public static BlockMegaTorch MegaTorch;
	public static BlockTerrainLighter TerrainLighter;

	public static void init()
	{
		MegaTorch = Register(new BlockMegaTorch());
		TerrainLighter = Register(new BlockTerrainLighter());
	}

	private static <T extends BlockBase> T Register(T block, ItemBlock itemBlock) {
		GameRegistry.register(block);
		GameRegistry.register(itemBlock);

		block.RegisterItemModel(itemBlock);

		return block;
	}

	private static <T extends BlockBase> T Register(T block) {
		ItemBlock itemBlock = new ItemBlock(block);
		itemBlock.setRegistryName(block.getRegistryName());
		block.setCreativeTab(CreativeTabs.DECORATIONS);
		return Register(block, itemBlock);
	}
}
