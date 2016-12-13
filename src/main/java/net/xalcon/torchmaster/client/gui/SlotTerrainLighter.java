package net.xalcon.torchmaster.client.gui;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.xalcon.torchmaster.common.tiles.TileEntityTerrainLighter;

import javax.annotation.Nullable;

public class SlotTerrainLighter extends Slot
{
	public SlotTerrainLighter(IInventory inventoryIn, int index, int xPosition, int yPosition)
	{
		super(inventoryIn, index, xPosition, yPosition);
	}

	/**
	 * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
	 */
	public boolean isItemValid(@Nullable ItemStack stack)
	{
		return TileEntityTerrainLighter.isItemAllowed(stack);
	}
}
