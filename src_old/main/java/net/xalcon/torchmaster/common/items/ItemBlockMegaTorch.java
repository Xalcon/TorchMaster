package net.xalcon.torchmaster.common.items;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.xalcon.torchmaster.common.TorchmasterConfig;

import javax.annotation.Nullable;
import java.util.List;

public class ItemBlockMegaTorch extends ItemBlockTooltipInfo
{
	public ItemBlockMegaTorch(Block block)
	{
		super(block);

		// this.setHasSubtypes(true);
	}

	/*@Override
	public int getMetadata(int damage)
	{
		// vanilla item blocks return "0" instead of the damage value
		return damage;
	}*/

	@Override
	public String getTranslationKey(ItemStack stack)
	{
		return super.getTranslationKey(stack);
		// TODO: get blockstate from itemstack
		//return super.getTranslationKey(stack) + "." + (ModBlocks.getMegaTorch().getStateFromMeta(stack.getMetadata()).getValue(BlockMegaTorch.BURNING) ? "lit" : "unlit");
	}

	/**
	 * allows items to add custom lines of information to the mouseover description
	 *
	 * @param stack
	 * @param worldIn
	 * @param tooltip
	 * @param flagIn
	 */
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
	{
		super.addInformation(stack, worldIn, tooltip, flagIn);
		Item item = stack.getItem();

		// TODO: get blockstate from itemstack
		//if(ModBlocks.getMegaTorch().getStateFromMeta(stack.getMetadata()).getValue(BlockMegaTorch.BURNING))
		if(false)
		{
			NBTTagCompound compound = stack.getChildTag("tm_tile");
			if(compound == null) return;
			if(compound.getBoolean("isExtinguished")) return;

			int burnValueLeft = compound.getInt("burnValueLeft");
			int burnRate = TorchmasterConfig.MegaTorchBurnoutRate;
			if(burnRate <= 0) return;
			int ticksLeft = burnValueLeft / burnRate;

			int days = ticksLeft / DAY_TICKS;
			int hours = (ticksLeft - days * DAY_TICKS) / HOUR_TICKS;
			int minutes = (ticksLeft - days * DAY_TICKS - hours * HOUR_TICKS) / MINUTE_TICKS;
			int seconds = (ticksLeft - days * DAY_TICKS - hours * HOUR_TICKS - minutes * MINUTE_TICKS) / SECOND_TICKS;

			tooltip.add(new TextComponentTranslation(this.getTranslationKey(stack) + ".burntime_left", days, fmt(hours), fmt(minutes), fmt(seconds)));
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
