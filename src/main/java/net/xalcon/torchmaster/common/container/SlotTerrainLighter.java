package net.xalcon.torchmaster.common.container;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.xalcon.torchmaster.common.tiles.TileEntityTerrainLighter;

import javax.annotation.Nullable;

public class SlotTerrainLighter extends SlotItemHandler
{
	public SlotTerrainLighter(IItemHandler itemHandler, int index, int xPosition, int yPosition)
	{
		super(itemHandler, index, xPosition, yPosition);
	}

	/**
	 * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
	 */
	public boolean isItemValid(@Nullable ItemStack stack)
	{
		return TileEntityTerrainLighter.isItemAllowed(stack);
	}
}
