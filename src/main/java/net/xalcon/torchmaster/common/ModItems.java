package net.xalcon.torchmaster.common;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.xalcon.torchmaster.TorchMasterMod;
import net.xalcon.torchmaster.common.items.ItemFrozenPearl;

@Mod.EventBusSubscriber(modid = TorchMasterMod.MODID)
@GameRegistry.ObjectHolder(TorchMasterMod.MODID)
public class ModItems
{
    @GameRegistry.ObjectHolder("frozen_pearl")
    public static ItemFrozenPearl frozenPearl;

    @SubscribeEvent
    public static void onRegisterItem(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().register(new ItemFrozenPearl().setRegistryName("frozen_pearl"));
    }

    @SubscribeEvent
    public static void registerItems(ModelRegistryEvent event)
    {
        ModelLoader.setCustomModelResourceLocation(frozenPearl, 0, new ModelResourceLocation(frozenPearl.getRegistryName(), "inventory"));
    }
}
