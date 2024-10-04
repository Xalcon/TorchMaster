package net.xalcon.torchmaster.common;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryObject;
import net.xalcon.torchmaster.Torchmaster;
import net.xalcon.torchmaster.TorchmasterConfig;
import net.xalcon.torchmaster.common.blocks.EntityBlockingLightBlock;
import net.xalcon.torchmaster.common.blocks.FeralFlareLanternBlock;
import net.xalcon.torchmaster.common.items.TMItemBlock;
import net.xalcon.torchmaster.common.logic.entityblocking.dreadlamp.DreadLampEntityBlockingLight;
import net.xalcon.torchmaster.common.logic.entityblocking.megatorch.MegatorchEntityBlockingLight;
import net.xalcon.torchmaster.common.tiles.FeralFlareLanternTileEntity;

public final class ModBlocks
{
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Torchmaster.MODID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Torchmaster.MODID);
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Torchmaster.MODID);

    public static RegistryObject<EntityBlockingLightBlock> blockMegaTorch = BLOCKS.register("megatorch", () ->
            new EntityBlockingLightBlock(Block.Properties
                    .of()
                    .sound(SoundType.WOOD)
                    .strength(1.0f, 1.0f)
                    .lightLevel(blockState -> 15),
                    pos -> "MT_" +pos.getX() + "_" + pos.getY() + "_" + pos.getZ(),
                    MegatorchEntityBlockingLight::new, 1.0f, MegatorchEntityBlockingLight.SHAPE,
                    TorchmasterConfig.GENERAL.megaTorchRadius
            )
    );
    public static RegistryObject<TMItemBlock> itemMegaTorch = fromBlock(blockMegaTorch,
            new Item.Properties());

    public static RegistryObject<EntityBlockingLightBlock> blockDreadLamp = BLOCKS.register("dreadlamp", () ->
            new EntityBlockingLightBlock(Block.Properties
                    .of()
                    .sound(SoundType.LANTERN)
                    .strength(1.0f, 1.0f)
                    .lightLevel(blockState -> 15),
                    pos -> "DL_" + pos.getX() + "_" + pos.getY() + "_" + pos.getZ(),
                    DreadLampEntityBlockingLight::new, 0.3f, DreadLampEntityBlockingLight.SHAPE,
                    TorchmasterConfig.GENERAL.dreadLampRadius
            )
    );
    public static RegistryObject<TMItemBlock> itemDreadLamp = fromBlock(blockDreadLamp,
            new Item.Properties());

    public static RegistryObject<FeralFlareLanternBlock> blockFeralFlareLantern = BLOCKS.register("feral_flare_lantern", () ->
            new FeralFlareLanternBlock(Block.Properties
                    .of()
                    .sound(SoundType.LANTERN)
                    .strength(1.0f, 1.0f)
                    .lightLevel(blockState -> 15)));
    public static RegistryObject<TMItemBlock> itemFeralFlareLantern = fromBlock(blockFeralFlareLantern, new Item.Properties());
    public static RegistryObject<BlockEntityType<FeralFlareLanternTileEntity>> tileFeralFlareLantern =
            BLOCK_ENTITIES.register("feral_flare_lantern", () ->
                    BlockEntityType.Builder
                            .of(FeralFlareLanternTileEntity::new, blockFeralFlareLantern.get())
                            .build(null));

    public static RegistryObject<AirBlock> blockInvisibleLight = BLOCKS.register("invisible_light", () ->
            new AirBlock(Block.Properties
                    .copy(Blocks.AIR)
                    .lightLevel(blockState -> 15)
                    .noCollission()
                    .air()));

    public static void init()
    {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        BLOCKS.register(bus);
        ITEMS.register(bus);
        BLOCK_ENTITIES.register(bus);
    }

    private ModBlocks() {}

    private static <B extends Block> RegistryObject<TMItemBlock> fromBlock(RegistryObject<B> block, Item.Properties properties) {
        return ITEMS.register(block.getId().getPath(), () -> new TMItemBlock(block.get(), properties));
    }
}
