package net.xalcon.torchmaster.common;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.xalcon.torchmaster.TorchMasterMod;
import net.xalcon.torchmaster.common.blocks.BlockBase;
import net.xalcon.torchmaster.common.blocks.BlockDreadLamp;
import net.xalcon.torchmaster.common.blocks.BlockMegaTorch;
import net.xalcon.torchmaster.common.blocks.BlockTerrainLighter;
import net.xalcon.torchmaster.common.tiles.IAutoRegisterTileEntity;

import java.util.Arrays;

@GameRegistry.ObjectHolder(TorchMasterMod.MODID)
@Mod.EventBusSubscriber
public class ModBlocks
{
	@GameRegistry.ObjectHolder(BlockMegaTorch.INTERNAL_NAME)
	private final static BlockMegaTorch MegaTorch = null;
	@GameRegistry.ObjectHolder(BlockTerrainLighter.INTERNAL_NAME)
	private final static BlockTerrainLighter TerrainLighter = null;
	@GameRegistry.ObjectHolder(BlockDreadLamp.INTERNAL_NAME)
	private final static BlockDreadLamp DreadLamp = null;

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

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event)
	{
		Block[] blocks = new Block[]
		{
			new BlockMegaTorch(),
			new BlockTerrainLighter(),
			new BlockDreadLamp()
		};

		event.getRegistry().registerAll(blocks);

		Arrays.stream(blocks)
			.filter(block -> block instanceof IAutoRegisterTileEntity)
			.map(block -> (IAutoRegisterTileEntity)block)
			.forEach(block -> GameRegistry.registerTileEntity(block.getTileEntityClass(), block.getTileEntityRegistryName()));
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		BlockBase[] blocks = new BlockBase[]
		{
			MegaTorch,
			TerrainLighter,
			DreadLamp
		};

        event.getRegistry().registerAll(Arrays.stream(blocks)
            .map(BlockBase::createItemBlock)
            .toArray(Item[]::new));
	}

	@SubscribeEvent
	public static void registerItems(ModelRegistryEvent event)
	{
		BlockBase[] blocks = new BlockBase[]
		{
			MegaTorch,
			TerrainLighter,
			DreadLamp
		};

        Arrays.stream(blocks)
            .forEach(block -> block.registerItemModels(Item.getItemFromBlock(block)));
    }


}
