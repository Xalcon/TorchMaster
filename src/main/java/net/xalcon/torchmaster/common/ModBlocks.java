package net.xalcon.torchmaster.common;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;
import net.xalcon.torchmaster.TorchMasterMod;
import net.xalcon.torchmaster.common.blocks.*;
import net.xalcon.torchmaster.common.tiles.IAutoRegisterTileEntity;

import java.util.Arrays;
import java.util.Objects;

@ObjectHolder(TorchMasterMod.MODID)
@Mod.EventBusSubscriber
public class ModBlocks
{
	@ObjectHolder(BlockMegaTorch.INTERNAL_NAME)
	private final static BlockMegaTorch MegaTorch = null;
	@ObjectHolder(BlockTerrainLighter.INTERNAL_NAME)
	private final static BlockTerrainLighter TerrainLighter = null;
	@ObjectHolder(BlockDreadLamp.INTERNAL_NAME)
	private final static BlockDreadLamp DreadLamp = null;
	@ObjectHolder(BlockFeralFlareLantern.INTERNAL_NAME)
	private final static BlockFeralFlareLantern FeralFlareLantern = null;
	@ObjectHolder(BlockInvisibleLight.INTERNAL_NAME)
	private final static BlockInvisibleLight InvisibleLight = null;

	public static BlockMegaTorch getMegaTorch()
	{
		return MegaTorch;
	}

	public static BlockTerrainLighter getTerrainLighter()
	{
		return TerrainLighter;
	}

	public static BlockDreadLamp getDreadLamp()
	{
		return DreadLamp;
	}

	public static BlockInvisibleLight getInvisibleLight()
	{
		return InvisibleLight;
	}

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event)
	{
		Block[] blocks = new Block[]
		{
			new BlockMegaTorch(),
			new BlockTerrainLighter(),
			new BlockDreadLamp(),
			new BlockFeralFlareLantern(),
            new BlockInvisibleLight()
		};

		event.getRegistry().registerAll(blocks);
	}

	public static void registerTileEntities(final RegistryEvent.Register<TileEntityType<?>> event)
	{
		Block[] blocks = new Block[]
		{
			MegaTorch,
			TerrainLighter,
			DreadLamp,
			FeralFlareLantern,
			InvisibleLight
		};

		Arrays.stream(blocks)
				.filter(block -> block instanceof IAutoRegisterTileEntity)
				.map(block -> (IAutoRegisterTileEntity)block)
				.forEach(block -> event.getRegistry().register(TileEntityType.Builder.create(block::createTileEntity).build(null)));
	}

	@SubscribeEvent
	public static void registerItems(final RegistryEvent.Register<Item> event)
	{
		BlockBase[] blocks = new BlockBase[]
		{
			MegaTorch,
			TerrainLighter,
			DreadLamp,
			FeralFlareLantern
		};

        event.getRegistry().registerAll(Arrays.stream(blocks)
            .map(BlockBase::createItemBlock)
			.filter(Objects::nonNull)
            .toArray(Item[]::new));
	}

	@SubscribeEvent
	public static void registerItems(ModelRegistryEvent event)
	{
		BlockBase[] blocks = new BlockBase[]
		{
			MegaTorch,
			TerrainLighter,
			DreadLamp,
			FeralFlareLantern
		};

        Arrays.stream(blocks)
            .forEach(block -> block.registerItemModels(Item.getItemFromBlock(block)));
    }


}
