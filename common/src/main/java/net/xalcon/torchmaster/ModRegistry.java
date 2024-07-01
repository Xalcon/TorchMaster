package net.xalcon.torchmaster;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.xalcon.torchmaster.blocks.*;
import net.xalcon.torchmaster.items.TMItemBlock;
import net.xalcon.torchmaster.platform.RegistrationProvider;
import net.xalcon.torchmaster.platform.RegistryObject;
import net.xalcon.torchmaster.platform.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings("DanglingJavadoc")
public class ModRegistry
{
    private static final RegistrationProvider<Block> BLOCKS = RegistrationProvider.create(Registries.BLOCK, Constants.MOD_ID);
    private static final RegistrationProvider<Item> ITEMS = RegistrationProvider.create(Registries.ITEM, Constants.MOD_ID);
    private static final RegistrationProvider<BlockEntityType<?>> BLOCK_ENTITIES = RegistrationProvider.create(Registries.BLOCK_ENTITY_TYPE, Constants.MOD_ID);
    private static final RegistrationProvider<CreativeModeTab> CREATIVE_MODE_TABS = RegistrationProvider.create(Registries.CREATIVE_MODE_TAB, Constants.MOD_ID);

    public static RegistryObject<EntityBlockingLightBlock> blockMegaTorch;
    public static RegistryObject<EntityBlockingLightBlock> blockDreadLamp;

    public static RegistryObject<FeralFlareLanternBlock> blockFeralFlareLantern;
    public static RegistryObject<BlockEntityType<FeralFlareLanternBlockEntity>> tileFeralFlareLantern;
    public static RegistryObject<InvisibleLightBlock> blockInvisibleLight;

    public static RegistryObject<Item> itemMegaTorch;
    public static RegistryObject<Item> itemDreadLamp;
    public static RegistryObject<Item> itemFeralFlareLantern;

    private ModRegistry() { }
    private static CreativeModeTab tab;

    public static void initialize()
    {
        List<RegistryObject<Item>> creativeTabItems = new ArrayList<>();

        /**
         Mega Torch
         */
        blockMegaTorch = BLOCKS.register("megatorch", () -> new EntityBlockingLightBlock(
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.COLOR_YELLOW)
                        .sound(SoundType.WOOD)
                        .strength(1.0f, 1.0f)
                        .lightLevel(state -> 15),
                LightType.MegaTorch));
        itemMegaTorch = fromBlock(blockMegaTorch);
        creativeTabItems.add(itemMegaTorch);

        /**
         Dread Lamp
         */
        blockDreadLamp = BLOCKS.register("dreadlamp", () -> new EntityBlockingLightBlock(
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.COLOR_BLACK)
                        .sound(SoundType.WOOD)
                        .strength(1.0f, 1.0f)
                        .lightLevel(state -> 15),
                LightType.DreadLamp));
        itemDreadLamp = fromBlock(blockDreadLamp);
        creativeTabItems.add(itemDreadLamp);

        /**
         Feral Flare Lantern
         */
        blockFeralFlareLantern = BLOCKS.register("feral_flare_lantern", () -> new FeralFlareLanternBlock(
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.COLOR_YELLOW)
                        .sound(SoundType.LANTERN)
                        .strength(1.0f, 1.0f)
                        .lightLevel(state -> 15)
                )
        );
        tileFeralFlareLantern = BLOCK_ENTITIES.register(blockFeralFlareLantern.getId().getPath(),
                () -> Services.PLATFORM.createBlockEntityType(FeralFlareLanternBlockEntity::new, blockFeralFlareLantern.get()));
        itemFeralFlareLantern = fromBlock(blockFeralFlareLantern);
        creativeTabItems.add(itemFeralFlareLantern);

        blockInvisibleLight = BLOCKS.register("invisible_light", () -> new InvisibleLightBlock(
                BlockBehaviour.Properties.of()
                        .lightLevel(state -> 15)
                        .noCollission()
                        .air()
                )
        );

        /**
         Creative Mode Tab
         */
        CREATIVE_MODE_TABS.register(Constants.MOD_ID, () -> Services.PLATFORM.createCreativeModeTab(Constants.MOD_ID, creativeTabItems));
    }

    private static <B extends Block> RegistryObject<Item> fromBlock(RegistryObject<B> block) {
        return fromBlock(block, i -> {});
    }

    private static <B extends Block> RegistryObject<Item> fromBlock(RegistryObject<B> block, Consumer<Item.Properties> propertiesConfig) {
        var properties = new Item.Properties();
        propertiesConfig.accept(properties);
        return ITEMS.register(block.getId().getPath(), () -> new TMItemBlock(block.get(), properties));
    }
}
