package net.xalcon.torchmaster.common;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.xalcon.torchmaster.TorchMasterMod;
import net.xalcon.torchmaster.common.blocks.*;
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
	@GameRegistry.ObjectHolder(BlockFeralFlareLantern.INTERNAL_NAME)
	private final static BlockFeralFlareLantern FeralFlareLantern = null;
	@GameRegistry.ObjectHolder(BlockInvisibleLight.INTERNAL_NAME)
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
			DreadLamp,
			FeralFlareLantern,
			InvisibleLight
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
			DreadLamp,
			FeralFlareLantern,
            InvisibleLight
		};

        Arrays.stream(blocks)
            .forEach(block -> block.registerItemModels(Item.getItemFromBlock(block)));
    }


}
