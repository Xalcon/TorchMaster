package net.xalcon.torchmaster.common.init;

import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;
import net.xalcon.torchmaster.Torchmaster;
import net.xalcon.torchmaster.common.items.ItemFrozenPearl;

@Mod.EventBusSubscriber(modid = Torchmaster.MODID)
@ObjectHolder(Torchmaster.MODID)
public class ModItems
{
    @ObjectHolder("frozen_pearl")
    public static ItemFrozenPearl frozenPearl;

    @SubscribeEvent
    public static void onRegisterItem(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().register(new ItemFrozenPearl().setRegistryName("frozen_pearl"));
    }

    @SubscribeEvent
    public static void registerItems(ModelRegistryEvent event)
    {
        // TODO: Item Model Registration
        //ModelLoader.setCustomModelResourceLocation(frozenPearl, 0, new ModelResourceLocation(frozenPearl.getRegistryName(), "inventory"));

    }
}
