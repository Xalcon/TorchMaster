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
import net.xalcon.torchmaster.common.logic.entityblocking.dreadlamp.DreadLampEntityBlockingLight;
import net.xalcon.torchmaster.common.logic.entityblocking.megatorch.MegatorchEntityBlockingLight;

import static net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@ObjectHolder(Torchmaster.MODID)
public final class ModBlocks
{
    @ObjectHolder("megatorch")
    public static EntityBlockingLightBlock blockMegaTorch;

    @ObjectHolder("megatorch")
    public static EntityBlockingLightItemBlock itemMegaTorch;

    @ObjectHolder("dreadlamp")
    public static EntityBlockingLightBlock blockDreadLamp;

    @ObjectHolder("dreadlamp")
    public static EntityBlockingLightItemBlock itemDreadLamp;

    @Mod.EventBusSubscriber(bus = Bus.MOD, modid = Torchmaster.MODID)
    private static class RegistryEvents
    {
        @SubscribeEvent
        public static void onRegisterBlocks(RegistryEvent.Register<Block> event)
        {
            blockMegaTorch = new EntityBlockingLightBlock(Block.Properties
                    .create(Material.WOOD)
                    .hardnessAndResistance(1.0f, 1.0f)
                    .lightValue(15),
                pos -> "MT_" +pos.getX() + "_" + pos.getY() + "_" + pos.getZ(),
                MegatorchEntityBlockingLight::new, 1.0f);
            blockMegaTorch.setRegistryName("megatorch");
            event.getRegistry().register(blockMegaTorch);

            blockDreadLamp = new EntityBlockingLightBlock(Block.Properties
                .create(Material.WOOD)
                .hardnessAndResistance(1.0f, 1.0f)
                .lightValue(15),
                pos -> "DL_" + pos.getX() + "_" + pos.getY() + "_" + pos.getZ(),
                DreadLampEntityBlockingLight::new, 0.3f);
            blockDreadLamp.setRegistryName("dreadlamp");
            event.getRegistry().register(blockDreadLamp);

        }

        @SubscribeEvent
        public static void onRegisterItems(RegistryEvent.Register<Item> event)
        {
            itemMegaTorch = new EntityBlockingLightItemBlock(blockMegaTorch, new Item.Properties().group(TorchmasterItemGroup.INSTANCE));
            event.getRegistry().register(itemMegaTorch);

            itemDreadLamp = new EntityBlockingLightItemBlock(blockDreadLamp, new Item.Properties().group(TorchmasterItemGroup.INSTANCE));
            event.getRegistry().register(itemDreadLamp);
        }
    }

    private ModBlocks() {}
}
