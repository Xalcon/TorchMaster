package net.xalcon.torchmaster.client;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.xalcon.torchmaster.common.CommonProxy;
import net.xalcon.torchmaster.common.blocks.BlockBase;

public class ClientProxy extends CommonProxy
{
	@Override
	public <T extends BlockBase> T registerBlock(T block)
	{
		T outBlock = super.registerBlock(block);
		Item item = ForgeRegistries.ITEMS.getValue(block.getRegistryName());
		if(!(item instanceof ItemBlock))
			throw new IllegalStateException("Something went horribly wrong while registering item renderer. ItemBlock for " + block.getRegistryName() + " has not been registered yet or is the wrong type");
		block.registerItemModels((ItemBlock) item, this::registerItemRenderer);
		return outBlock;
	}

	private void registerItemRenderer(Item item, int meta, ResourceLocation registryName, String variantName)
	{
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(registryName, variantName));
	}
}
