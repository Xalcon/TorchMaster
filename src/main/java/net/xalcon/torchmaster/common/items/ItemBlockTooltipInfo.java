package net.xalcon.torchmaster.common.items;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.xalcon.torchmaster.common.TorchmasterConfig;
import net.xalcon.torchmaster.common.itemgroups.ItemGroupTorchMaster;

import javax.annotation.Nullable;
import java.util.List;

public class ItemBlockTooltipInfo extends ItemBlock
{
    public ItemBlockTooltipInfo(Block block)
    {
        super(block, new Item.Builder().group(ItemGroupTorchMaster.INSTANCE));
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
        if(TorchmasterConfig.BeginnerTooltips)
            tooltip.add(new TextComponentTranslation(this.getTranslationKey(stack) + ".tooltip"));
    }
}
