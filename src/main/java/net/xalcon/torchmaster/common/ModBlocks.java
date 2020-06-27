package net.xalcon.torchmaster.common;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import net.xalcon.torchmaster.Torchmaster;
import net.xalcon.torchmaster.common.blocks.EntityBlockingLightBlock;
import net.xalcon.torchmaster.common.blocks.FeralFlareLanternBlock;
import net.xalcon.torchmaster.common.blocks.InvisibleLightBlock;
import net.xalcon.torchmaster.common.items.TMItemBlock;
import net.xalcon.torchmaster.common.logic.entityblocking.dreadlamp.DreadLampEntityBlockingLight;
import net.xalcon.torchmaster.common.logic.entityblocking.megatorch.MegatorchEntityBlockingLight;
import net.xalcon.torchmaster.common.tiles.FeralFlareLanternTileEntity;

import java.rmi.registry.Registry;

import static net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@ObjectHolder(Torchmaster.MODID)
public final class ModBlocks
{
    @ObjectHolder("megatorch")
    public static EntityBlockingLightBlock blockMegaTorch;
    @ObjectHolder("megatorch")
    public static TMItemBlock itemMegaTorch;

    @ObjectHolder("dreadlamp")
    public static EntityBlockingLightBlock blockDreadLamp;
    @ObjectHolder("dreadlamp")
    public static TMItemBlock itemDreadLamp;

    @ObjectHolder("feral_flare_lantern")
    public static FeralFlareLanternBlock blockFeralFlareLantern;
    @ObjectHolder("feral_flare_lantern")
    public static TMItemBlock itemFeralFlareLantern;
    @ObjectHolder("feral_flare_lantern")
    public static TileEntityType<FeralFlareLanternTileEntity> tileFeralFlareLantern;

    @ObjectHolder("invisible_light")
    public static InvisibleLightBlock blockInvisibleLight;

    @Mod.EventBusSubscriber(bus = Bus.MOD, modid = Torchmaster.MODID)
    private static class RegistryEvents
    {
        @SubscribeEvent
        public static void onRegisterBlocks(RegistryEvent.Register<Block> event)
        {
            IForgeRegistry<Block> registry = event.getRegistry();

            /* Mega Torch */
            blockMegaTorch = new EntityBlockingLightBlock(Block.Properties
                    .create(Material.WOOD)
                    .hardnessAndResistance(1.0f, 1.0f)
                    .lightValue(15),
                pos -> "MT_" +pos.getX() + "_" + pos.getY() + "_" + pos.getZ(),
                MegatorchEntityBlockingLight::new, 1.0f, MegatorchEntityBlockingLight.SHAPE);
            blockMegaTorch.setRegistryName("megatorch");
            registry.register(blockMegaTorch);

            /* Dread Lamp */
            blockDreadLamp = new EntityBlockingLightBlock(Block.Properties
                .create(Material.WOOD)
                .hardnessAndResistance(1.0f, 1.0f)
                .lightValue(15),
                pos -> "DL_" + pos.getX() + "_" + pos.getY() + "_" + pos.getZ(),
                DreadLampEntityBlockingLight::new, 0.3f, DreadLampEntityBlockingLight.SHAPE);
            blockDreadLamp.setRegistryName("dreadlamp");
            registry.register(blockDreadLamp);

            /* Feral Flare Lantern */
            blockFeralFlareLantern = new FeralFlareLanternBlock(Block.Properties
                .create(Material.WOOD)
                .hardnessAndResistance(1.0f, 1.0f)
                .lightValue(15));
            blockFeralFlareLantern.setRegistryName("feral_flare_lantern");
            registry.register(blockFeralFlareLantern);

            /* Invisible light */
            blockInvisibleLight = new InvisibleLightBlock(Block.Properties
                .create(Material.AIR)
                .hardnessAndResistance(0f, 0f)
                .lightValue(15));
            blockInvisibleLight.setRegistryName("invisible_light");
            registry.register(blockInvisibleLight);
        }

        @SubscribeEvent
        public static void onRegisterItems(RegistryEvent.Register<Item> event)
        {
            IForgeRegistry<Item> registry = event.getRegistry();

            itemMegaTorch = new TMItemBlock(blockMegaTorch, new Item.Properties().group(TorchmasterItemGroup.INSTANCE));
            registry.register(itemMegaTorch);

            itemDreadLamp = new TMItemBlock(blockDreadLamp, new Item.Properties().group(TorchmasterItemGroup.INSTANCE));
            registry.register(itemDreadLamp);

            itemFeralFlareLantern = new TMItemBlock(blockFeralFlareLantern, new Item.Properties().group(TorchmasterItemGroup.INSTANCE));
            registry.register(itemFeralFlareLantern);
        }

        @SubscribeEvent
        public static void onRegisterTileEntites(RegistryEvent.Register<TileEntityType<?>> event)
        {
            event.getRegistry().register(TileEntityType.Builder.create(FeralFlareLanternTileEntity::new, blockFeralFlareLantern).build(null).setRegistryName(blockFeralFlareLantern.getRegistryName()));
        }
    }

    private ModBlocks() {}
}
