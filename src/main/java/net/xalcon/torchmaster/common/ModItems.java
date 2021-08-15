package net.xalcon.torchmaster.common;

import net.minecraft.world.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;
import net.xalcon.torchmaster.Torchmaster;
import net.xalcon.torchmaster.common.items.FrozenPearlItem;

@ObjectHolder(Torchmaster.MODID)
public class ModItems
{
    @ObjectHolder("frozen_pearl")
    public static FrozenPearlItem itemFrozenPearl;

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = Torchmaster.MODID)
    private static class RegistryEvents
    {
        @SubscribeEvent
        public static void onRegisterItems(RegistryEvent.Register<Item> event)
        {
            itemFrozenPearl = new FrozenPearlItem(new Item.Properties().tab(TorchmasterItemGroup.INSTANCE));
            itemFrozenPearl.setRegistryName("frozen_pearl");
            event.getRegistry().register(itemFrozenPearl);
        }
    }
}
