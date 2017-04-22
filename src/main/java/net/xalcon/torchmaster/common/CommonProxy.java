package net.xalcon.torchmaster.common;

import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.xalcon.torchmaster.common.blocks.BlockBase;
import net.xalcon.torchmaster.common.tiles.IAutoRegisterTileEntity;

public abstract class CommonProxy
{
	public <T extends BlockBase> T registerBlock(T block)
	{
		ItemBlock itemBlock = new ItemBlock(block);
		ResourceLocation registryName = block.getRegistryName();
		if (registryName == null)
			throw new NullPointerException("Block registry name must not be null! Blame the developer (Block: " + block + ")");
		itemBlock.setRegistryName(registryName);
		return registerBlock(block, itemBlock);
	}

	public <T extends BlockBase> T registerBlock(T block, ItemBlock itemBlock)
	{
		GameRegistry.register(block);
		GameRegistry.register(itemBlock);

		if (block instanceof IAutoRegisterTileEntity)
		{
			IAutoRegisterTileEntity tile = (IAutoRegisterTileEntity) block;
			GameRegistry.registerTileEntity(tile.getTileEntityClass(), tile.getTileEntityRegistryName());
		}

		return block;
	}
}
