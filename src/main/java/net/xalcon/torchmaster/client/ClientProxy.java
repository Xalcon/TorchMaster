package net.xalcon.torchmaster.client;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.xalcon.torchmaster.common.CommonProxy;
import net.xalcon.torchmaster.common.blocks.BlockBase;

public class ClientProxy extends CommonProxy
{
	@Override
	public <T extends BlockBase> T registerBlock(T block, ItemBlock itemBlock)
	{
		T outBlock = super.registerBlock(block, itemBlock);
		block.registerItemModels(itemBlock, this::registerItemRenderer);
		return outBlock;
	}

	private void registerItemRenderer(Item item, int meta, ResourceLocation registryName, String variantName)
	{
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(registryName, variantName));
	}
}
