package net.xalcon.torchmaster.common;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import net.xalcon.torchmaster.Torchmaster;
import net.xalcon.torchmaster.common.blocks.EntityBlockingLightBlock;
import net.xalcon.torchmaster.common.blocks.FeralFlareLanternBlock;
import net.xalcon.torchmaster.common.items.TMItemBlock;
import net.xalcon.torchmaster.common.logic.entityblocking.dreadlamp.DreadLampEntityBlockingLight;
import net.xalcon.torchmaster.common.logic.entityblocking.megatorch.MegatorchEntityBlockingLight;
import net.xalcon.torchmaster.common.tiles.FeralFlareLanternTileEntity;

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
    public static BlockEntityType<FeralFlareLanternTileEntity> tileFeralFlareLantern;

    @ObjectHolder("invisible_light")
    public static AirBlock blockInvisibleLight;

    @Mod.EventBusSubscriber(bus = Bus.MOD, modid = Torchmaster.MODID)
    private static class RegistryEvents
    {
        @SubscribeEvent
        public static void onRegisterBlocks(RegistryEvent.Register<Block> event)
        {
            IForgeRegistry<Block> registry = event.getRegistry();

            /* Mega Torch */
            blockMegaTorch = new EntityBlockingLightBlock(Block.Properties
                    .of(Material.WOOD)
                    .strength(1.0f, 1.0f)
                    .lightLevel(blockState -> 15),
                pos -> "MT_" +pos.getX() + "_" + pos.getY() + "_" + pos.getZ(),
                MegatorchEntityBlockingLight::new, 1.0f, MegatorchEntityBlockingLight.SHAPE);
            blockMegaTorch.setRegistryName("megatorch");
            registry.register(blockMegaTorch);

            /* Dread Lamp */
            blockDreadLamp = new EntityBlockingLightBlock(Block.Properties
                .of(Material.WOOD)
                .strength(1.0f, 1.0f)
                .lightLevel(blockState -> 15),
                pos -> "DL_" + pos.getX() + "_" + pos.getY() + "_" + pos.getZ(),
                DreadLampEntityBlockingLight::new, 0.3f, DreadLampEntityBlockingLight.SHAPE);
            blockDreadLamp.setRegistryName("dreadlamp");
            registry.register(blockDreadLamp);

            /* Feral Flare Lantern */
            blockFeralFlareLantern = new FeralFlareLanternBlock(Block.Properties
                .of(Material.WOOD)
                .strength(1.0f, 1.0f)
                .lightLevel(blockState -> 15));
            blockFeralFlareLantern.setRegistryName("feral_flare_lantern");
            registry.register(blockFeralFlareLantern);

            /* Invisible light */
            blockInvisibleLight = new AirBlock(Block.Properties
                .of(Material.AIR)
                .lightLevel(blockState -> 15)
                .noCollission()
                .noDrops()
                .air());
            blockInvisibleLight.setRegistryName("invisible_light");
            registry.register(blockInvisibleLight);


        }

        @SubscribeEvent
        public static void onRegisterItems(RegistryEvent.Register<Item> event)
        {
            IForgeRegistry<Item> registry = event.getRegistry();

            itemMegaTorch = new TMItemBlock(blockMegaTorch, new Item.Properties().tab(TorchmasterCreativeTab.INSTANCE));
            registry.register(itemMegaTorch);

            itemDreadLamp = new TMItemBlock(blockDreadLamp, new Item.Properties().tab(TorchmasterCreativeTab.INSTANCE));
            registry.register(itemDreadLamp);

            itemFeralFlareLantern = new TMItemBlock(blockFeralFlareLantern, new Item.Properties().tab(TorchmasterCreativeTab.INSTANCE));
            registry.register(itemFeralFlareLantern);
        }

        @SubscribeEvent
        public static void onRegisterTileEntites(RegistryEvent.Register<BlockEntityType<?>> event)
        {
            event.getRegistry().register(
                    BlockEntityType.Builder
                            .of(FeralFlareLanternTileEntity::new, blockFeralFlareLantern)
                            .build(null)
                            .setRegistryName(blockFeralFlareLantern.getRegistryName()));
        }
    }

    private ModBlocks() {}
}
