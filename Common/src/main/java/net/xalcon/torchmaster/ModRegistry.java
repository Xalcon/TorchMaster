package net.xalcon.torchmaster;

import net.minecraft.core.Registry;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Material;
import net.xalcon.torchmaster.blocks.EntityBlockingLightBlock;
import net.xalcon.torchmaster.blocks.FeralFlareLanternBlock;
import net.xalcon.torchmaster.blocks.FeralFlareLanternTileEntity;
import net.xalcon.torchmaster.blocks.InvisibleLightBlock;
import net.xalcon.torchmaster.items.FrozenPearlItem;
import net.xalcon.torchmaster.items.TMItemBlock;
import net.xalcon.torchmaster.logic.entityblocking.dreadlamp.DreadLampEntityBlockingLight;
import net.xalcon.torchmaster.logic.entityblocking.megatorch.MegatorchEntityBlockingLight;
import net.xalcon.torchmaster.platform.Services;
import net.xalcon.torchmaster.platform.registration.RegistrationProvider;
import net.xalcon.torchmaster.platform.registration.RegistryObject;

public class ModRegistry
{
    private static final RegistrationProvider<Block> BLOCKS = RegistrationProvider.create(Registry.BLOCK_REGISTRY, Constants.MOD_ID);
    private static final RegistrationProvider<Item> ITEMS = RegistrationProvider.create(Registry.ITEM_REGISTRY, Constants.MOD_ID);
    private static final RegistrationProvider<BlockEntityType<?>> BLOCK_ENTITIES = RegistrationProvider.create(Registry.BLOCK_ENTITY_TYPE_REGISTRY, Constants.MOD_ID);

    public static RegistryObject<EntityBlockingLightBlock> blockMegaTorch = BLOCKS.register("megatorch", () ->
            new EntityBlockingLightBlock(Block.Properties
                    .of(Material.WOOD)
                    .sound(SoundType.WOOD)
                    .strength(1.0f, 1.0f)
                    .lightLevel(blockState -> 15),
                    pos -> "MT_" +pos.getX() + "_" + pos.getY() + "_" + pos.getZ(),
                    MegatorchEntityBlockingLight::new, 1.0f, MegatorchEntityBlockingLight.SHAPE));
    public static RegistryObject<TMItemBlock> itemMegaTorch = fromBlock(blockMegaTorch,
            new Item.Properties().tab(Services.PLATFORM.getCreativeTab()));

    public static RegistryObject<EntityBlockingLightBlock> blockDreadLamp = BLOCKS.register("dreadlamp", () ->
            new EntityBlockingLightBlock(Block.Properties
                    .of(Material.METAL)
                    .sound(SoundType.LANTERN)
                    .strength(1.0f, 1.0f)
                    .lightLevel(blockState -> 15),
                    pos -> "DL_" + pos.getX() + "_" + pos.getY() + "_" + pos.getZ(),
                    DreadLampEntityBlockingLight::new, 0.3f, DreadLampEntityBlockingLight.SHAPE));
    public static RegistryObject<TMItemBlock> itemDreadLamp = fromBlock(blockDreadLamp,
            new Item.Properties().tab(Services.PLATFORM.getCreativeTab()));

    public static RegistryObject<FeralFlareLanternBlock> blockFeralFlareLantern = BLOCKS.register("feral_flare_lantern", () ->
            new FeralFlareLanternBlock(Block.Properties
                    .of(Material.METAL)
                    .sound(SoundType.LANTERN)
                    .strength(1.0f, 1.0f)
                    .lightLevel(blockState -> 15)));
    public static RegistryObject<TMItemBlock> itemFeralFlareLantern = fromBlock(blockFeralFlareLantern,
            new Item.Properties().tab(Services.PLATFORM.getCreativeTab()));
    public static RegistryObject<BlockEntityType<FeralFlareLanternTileEntity>> tileFeralFlareLantern =
            BLOCK_ENTITIES.register("feral_flare_lantern", () ->
                    Services.PLATFORM.createBlockEntityType(FeralFlareLanternTileEntity::new, blockFeralFlareLantern.get())
            );

    public static RegistryObject<AirBlock> blockInvisibleLight = BLOCKS.register("invisible_light", () ->
            new InvisibleLightBlock(Block.Properties
                    .of(Material.AIR)
                    .lightLevel(blockState -> 15)
                    .noCollission()
                    .air()));

    public static RegistryObject<FrozenPearlItem> itemFrozenPearl = ITEMS.register("frozen_pearl",
            () -> new FrozenPearlItem(new Item.Properties().tab(Services.PLATFORM.getCreativeTab())));

    private ModRegistry() {}

    private static <B extends Block> RegistryObject<TMItemBlock> fromBlock(RegistryObject<B> block, Item.Properties properties) {
        return ITEMS.register(block.getId().getPath(), () -> new TMItemBlock(block.get(), properties));
    }

    // This forces class loading, keep it here
    public static void init() { }
}
