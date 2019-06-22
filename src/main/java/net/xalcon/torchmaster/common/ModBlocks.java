package net.xalcon.torchmaster.common;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;
import net.xalcon.torchmaster.Torchmaster;
import net.xalcon.torchmaster.common.blocks.EntityBlockingLightBlock;
import net.xalcon.torchmaster.common.items.EntityBlockingLightItemBlock;

import static net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@ObjectHolder(Torchmaster.MODID)
public final class ModBlocks
{
    @ObjectHolder("megatorch")
    public static EntityBlockingLightBlock blockMegaTorch;

    @ObjectHolder("megatorch")
    public static EntityBlockingLightItemBlock itemMegaTorch;

    @Mod.EventBusSubscriber(bus = Bus.MOD, modid = Torchmaster.MODID)
    private static class RegistryEvents
    {
        @SubscribeEvent
        public static void onRegisterBlocks(RegistryEvent.Register<Block> event)
        {
            blockMegaTorch = new EntityBlockingLightBlock(Block.Properties
                    .create(Material.WOOD)
                    .hardnessAndResistance(1.0f, 1.0f)
                    .lightValue(15));
            blockMegaTorch.setRegistryName("megatorch");

            event.getRegistry().register(blockMegaTorch);
        }

        @SubscribeEvent
        public static void onRegisterItems(RegistryEvent.Register<Item> event)
        {
            itemMegaTorch = new EntityBlockingLightItemBlock(blockMegaTorch, new Item.Properties().group(TorchmasterItemGroup.INSTANCE));

            event.getRegistry().register(itemMegaTorch);
        }
    }

    private ModBlocks() {}
}
