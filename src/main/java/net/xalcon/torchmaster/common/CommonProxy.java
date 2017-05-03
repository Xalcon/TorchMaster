package net.xalcon.torchmaster.common;

import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.xalcon.torchmaster.common.blocks.BlockBase;
import net.xalcon.torchmaster.common.tiles.IAutoRegisterTileEntity;

public class CommonProxy
{
	public <T extends BlockBase> T registerBlock(T block)
	{
		ItemBlock itemBlock = block.createItemBlock();
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
