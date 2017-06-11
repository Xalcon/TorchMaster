package net.xalcon.torchmaster.common.items;

import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.xalcon.torchmaster.TorchMasterMod;
import net.xalcon.torchmaster.common.ModBlocks;
import net.xalcon.torchmaster.common.blocks.BlockMegaTorch;

import javax.annotation.Nullable;
import java.util.List;

public class ItemBlockMegaTorch extends ItemBlock
{
	public ItemBlockMegaTorch(Block block)
	{
		super(block);

		this.setHasSubtypes(true);
	}

	@Override
	public int getMetadata(int damage)
	{
		// vanilla item blocks return "0" instead of the damage value
		return damage;
	}

	public String getUnlocalizedName(ItemStack stack)
	{
		return super.getUnlocalizedName() + "." + (ModBlocks.MegaTorch.getStateFromMeta(stack.getMetadata()).getValue(BlockMegaTorch.BURNING) ? "lit" : "unlit");
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced)
	{
		super.addInformation(stack, playerIn, tooltip, advanced);
		if(ModBlocks.MegaTorch.getStateFromMeta(stack.getMetadata()).getValue(BlockMegaTorch.BURNING))
		{
			NBTTagCompound compound = stack.getSubCompound("tm_tile");
			if(compound == null) return;
			if(compound.getBoolean("isExtinguished")) return;

			int burnValueLeft = compound.getInteger("burnValueLeft");
			int burnRate = TorchMasterMod.ConfigHandler.getMegaTorchBurnoutRate();
			if(burnRate <= 0) return;
			int ticksLeft = burnValueLeft / burnRate;

			int days = ticksLeft / DAY_TICKS;
			int hours = (ticksLeft - days * DAY_TICKS) / HOUR_TICKS;
			int minutes = (ticksLeft - days * DAY_TICKS - hours * HOUR_TICKS) / MINUTE_TICKS;
			int seconds = (ticksLeft - days * DAY_TICKS - hours * HOUR_TICKS - minutes * MINUTE_TICKS) / SECOND_TICKS;

			tooltip.add(I18n.format(this.getUnlocalizedName(stack) + ".burntime_left", days, fmt(hours), fmt(minutes), fmt(seconds)));
		}
	}

	private final static int DAY_TICKS = 24 * 60 * 60 * 20;
	private final static int HOUR_TICKS = 60 * 60 * 20;
	private final static int MINUTE_TICKS = 60 * 20;
	private final static int SECOND_TICKS = 20;

	private static String fmt(int number)
	{
		return String.format("%02d", number);
	}
}
