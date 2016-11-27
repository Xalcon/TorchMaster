package net.xalcon.torchmaster.client;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.xalcon.torchmaster.TorchMasterMod;
import net.xalcon.torchmaster.common.Proxy;

public class ClientProxy extends Proxy
{
	@Override
	public void RegisterItemRenderer(Item item, int meta, String id)
	{
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(TorchMasterMod.MODID + ":" + id, "inventory"));
	}
}
