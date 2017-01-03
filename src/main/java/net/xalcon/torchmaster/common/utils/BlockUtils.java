package net.xalcon.torchmaster.common.utils;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntityMobSpawner;

public class BlockUtils
{
	public static IBlockState getBlockStateFromItemStack(ItemStack itemStack)
	{
		Block block = Block.getBlockFromItem(itemStack.getItem());
		if (block == null) return null;
		return block.getStateFromMeta(itemStack.getMetadata());
	}

	// Algorithm by mako @ http://stackoverflow.com/a/14010215
	public static int[] createSpiralMap(int radius)
	{
		int x = 0, y = 0, layer = 1, arm = 0;
		int[] array = new int[((radius + radius + 1) * (radius + radius + 1)) * 2];
		for (int i = 0; i < array.length; i += 2)
		{
			array[i] = x;
			array[i+1] = y;
			switch(arm)
			{
				case 0: ++x; if(x == layer) ++arm; break;
				case 1: ++y; if(y == layer) ++arm; break;
				case 2: --x; if(-x == layer) ++arm; break;
				case 3: --y; if(-y == layer) {++layer; arm = 0;} break;
			}
		}
		return array;
	}

	/**
	 * Vanilla MobSpawners use 2 sets of data to spawn mobs
	 * # SpawnData: The next mob that will spawn
	 * # SpawnPotentials: A list of mobs that can spawn, including a probability value
	 * This method adds a tag to each spawnable mob
	 * @param tag the tag to add to the "Tags" list
	 * @param spawner the spawner to modify
	 */
	public static void addTagToSpawner(String tag, TileEntityMobSpawner spawner)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		spawner.getSpawnerBaseLogic().writeToNBT(nbt);
		// Modify SpawnData
		NBTTagCompound spawnDataNbt = nbt.getCompoundTag("SpawnData");
		addTagToEntity(tag, spawnDataNbt);

		// Modify SpawnPotentials
		// SpawnPotentials is a NBTTagList of NBTTagCompounds
		NBTTagList spawnPotentialNbt = nbt.getTagList("SpawnPotentials", 10);
		for(int i = 0; i < spawnPotentialNbt.tagCount(); i++)
		{
			NBTTagCompound weightEntry = spawnPotentialNbt.getCompoundTagAt(i);
			// each tag compound has an entity tag (NBTTagCompound) and an weight tag (Integer)
			NBTTagCompound entityTag = weightEntry.getCompoundTag("Entity");
			addTagToEntity(tag, entityTag);
		}

		// force the tileEntity to read back the modified nbt
		spawner.getSpawnerBaseLogic().readFromNBT(nbt);
	}

	private static NBTTagCompound addTagToEntity(String tag, NBTTagCompound entity)
	{
		NBTTagList entityTagsList = entity.getTagList("Tags", 8);
		if(!NbtUtils.isStringInTagList(tag, entityTagsList))
		{
			entityTagsList.appendTag(new NBTTagString(tag));
			// we need to make sure that the TagList is added to the compound since getTagList()
			// will return an empty (and non-inserted) list if the requested list was not found
			// in the compound
			if(!entity.hasKey("Tags"))
				entity.setTag("Tags", entityTagsList);
		}
		return entity;
	}
}
