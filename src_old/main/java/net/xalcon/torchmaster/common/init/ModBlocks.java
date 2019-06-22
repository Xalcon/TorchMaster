package net.xalcon.torchmaster.common.init;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;
import net.xalcon.torchmaster.Torchmaster;
import net.xalcon.torchmaster.common.blocks.*;
import net.xalcon.torchmaster.common.itemgroups.ItemGroupTorchMaster;

import java.util.Arrays;
import java.util.Objects;

@ObjectHolder(Torchmaster.MODID)
@Mod.EventBusSubscriber
public class ModBlocks
{
	@ObjectHolder(BlockMegaTorch.INTERNAL_NAME)
	public static BlockMegaTorch MegaTorch = null;
	@ObjectHolder(BlockTerrainLighter.INTERNAL_NAME)
	public static BlockTerrainLighter TerrainLighter = null;
	@ObjectHolder(BlockDreadLamp.INTERNAL_NAME)
	public static BlockDreadLamp DreadLamp = null;
	@ObjectHolder(BlockFeralFlareLantern.INTERNAL_NAME)
	public static BlockFeralFlareLantern FeralFlareLantern = null;
	@ObjectHolder(BlockInvisibleLight.INTERNAL_NAME)
	public static BlockInvisibleLight InvisibleLight = null;

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event)
	{
		Block[] blocks = new Block[]
		{
			MegaTorch = new BlockMegaTorch(),
			TerrainLighter = new BlockTerrainLighter(),
			DreadLamp = new BlockDreadLamp(),
			FeralFlareLantern = new BlockFeralFlareLantern(),
            InvisibleLight = new BlockInvisibleLight()
		};

		event.getRegistry().registerAll(blocks);
	}

	@SubscribeEvent
	public static void registerItems(final RegistryEvent.Register<Item> event)
	{
		Block[] blocks = new Block[]
		{
			MegaTorch,
			TerrainLighter,
			DreadLamp,
			FeralFlareLantern
		};

        event.getRegistry().registerAll(Arrays.stream(blocks)
            .map(b -> new ItemBlock(b, new Item.Builder().group(ItemGroupTorchMaster.INSTANCE)).setRegistryName(b.getRegistryName()))
            .toArray(Item[]::new));
	}

	@SubscribeEvent
	public static void registerItemModels(ModelRegistryEvent event)
	{
		Block[] blocks = new Block[]
		{
			MegaTorch,
			TerrainLighter,
			DreadLamp,
			FeralFlareLantern
		};

        //Arrays.stream(blocks).forEach(block -> block.registerItemModels(block.asItem()));
    }
}
