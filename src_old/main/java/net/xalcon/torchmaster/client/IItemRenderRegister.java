package net.xalcon.torchmaster.client;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public interface IItemRenderRegister
{
	/**
	 * Registers an item renderer for the given item
	 *
	 * @param item         the item
	 * @param meta         meta id (0-x)
	 * @param registryName the registry name of the model
	 * @param variantName  the variant name in the model
	 */
	@SuppressWarnings("SameParameterValue")
	void registerItemRenderer(Item item, int meta, ResourceLocation registryName, String variantName);
}
