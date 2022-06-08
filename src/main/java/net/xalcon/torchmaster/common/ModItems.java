package net.xalcon.torchmaster.common;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.xalcon.torchmaster.Torchmaster;
import net.xalcon.torchmaster.common.items.FrozenPearlItem;

public class ModItems
{
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Torchmaster.MODID);

    public static void init()
    {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ITEMS.register(bus);
    }

    public static RegistryObject<FrozenPearlItem> itemFrozenPearl = ITEMS.register("frozen_pearl",
            () -> new FrozenPearlItem(new Item.Properties().tab(TorchmasterCreativeTab.INSTANCE)));
}
