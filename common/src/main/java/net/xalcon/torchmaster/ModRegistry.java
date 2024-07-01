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
import net.xalcon.torchmaster.blocks.LightType;
import net.xalcon.torchmaster.blocks.SpawnControllingBlock;
import net.xalcon.torchmaster.platform.RegistrationProvider;
import net.xalcon.torchmaster.platform.RegistryObject;
import net.xalcon.torchmaster.platform.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ModRegistry
{
    private static final RegistrationProvider<Block> BLOCKS = RegistrationProvider.create(Registries.BLOCK, Constants.MOD_ID);
    private static final RegistrationProvider<Item> ITEMS = RegistrationProvider.create(Registries.ITEM, Constants.MOD_ID);
    private static final RegistrationProvider<BlockEntityType<?>> BLOCK_ENTITIES = RegistrationProvider.create(Registries.BLOCK_ENTITY_TYPE, Constants.MOD_ID);
    private static final RegistrationProvider<CreativeModeTab> CREATIVE_MODE_TABS = RegistrationProvider.create(Registries.CREATIVE_MODE_TAB, Constants.MOD_ID);


    public static RegistryObject<SpawnControllingBlock> blockMegaTorch;
    public static RegistryObject<SpawnControllingBlock> blockDreadLamp;

    public static RegistryObject<Item> itemMegaTorch;
    public static RegistryObject<Item> itemDreadLamp;

    private ModRegistry() { }
    private static CreativeModeTab tab;

    public static void initialize()
    {
        List<RegistryObject<Item>> creativeTabItems = new ArrayList<>();

        blockMegaTorch = BLOCKS.register("megatorch", () -> new SpawnControllingBlock(
                BlockBehaviour.Properties.of()
                        .sound(SoundType.WOOD)
                        .strength(1.0f, 1.0f)
                        .lightLevel(state -> 15),
                LightType.MegaTorch));
        itemMegaTorch = fromBlock(blockMegaTorch);
        creativeTabItems.add(itemMegaTorch);

        blockDreadLamp = BLOCKS.register("dreadlamp", () -> new SpawnControllingBlock(
                BlockBehaviour.Properties.of()
                        .sound(SoundType.WOOD)
                        .strength(1.0f, 1.0f)
                        .lightLevel(state -> 15),
                LightType.DreadLamp));
        itemDreadLamp = fromBlock(blockDreadLamp);
        creativeTabItems.add(itemDreadLamp);

        CREATIVE_MODE_TABS.register(Constants.MOD_ID, () -> Services.PLATFORM.createCreativeModeTab(Constants.MOD_ID, creativeTabItems));
    }

    private static <B extends Block> RegistryObject<Item> fromBlock(RegistryObject<B> block) {
        return fromBlock(block, i -> {});
    }

    private static <B extends Block> RegistryObject<Item> fromBlock(RegistryObject<B> block, Consumer<Item.Properties> propertiesConfig) {
        var properties = new Item.Properties();
        propertiesConfig.accept(properties);
        return ITEMS.register(block.getId().getPath(), () -> new BlockItem(block.get(), properties));
    }
}
