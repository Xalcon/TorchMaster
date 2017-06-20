package net.xalcon.torchmaster.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.xalcon.torchmaster.TorchMasterMod;
import net.xalcon.torchmaster.client.IItemRenderRegister;
import net.xalcon.torchmaster.common.creativetabs.CreativeTabTorchMaster;

public class BlockBase extends Block
{
	public BlockBase(Material material, String internalName)
	{
		super(material);
		setUnlocalizedName(TorchMasterMod.MODID + "." + internalName);
		setRegistryName(internalName);
		setCreativeTab(CreativeTabTorchMaster.INSTANCE);
	}

	@SideOnly(Side.CLIENT)
	public void registerItemModels(Item item)
	{
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(this.getRegistryName(), "inventory"));
	}

	public Item createItemBlock()
	{
		return new ItemBlock(this).setRegistryName(this.getRegistryName());
	}
}

