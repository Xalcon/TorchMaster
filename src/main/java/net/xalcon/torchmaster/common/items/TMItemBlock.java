package net.xalcon.torchmaster.common.items;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.xalcon.torchmaster.TorchmasterConfig;

import javax.annotation.Nullable;
import java.util.List;

public class TMItemBlock extends BlockItem
{
    public TMItemBlock(Block blockIn, Properties builder)
    {
        super(blockIn, builder);
        this.setRegistryName(blockIn.getRegistryName());
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag)
    {
        super.addInformation(stack, world, tooltip, flag);
        if(TorchmasterConfig.GENERAL.beginnerTooltips.get())
            tooltip.add(new TranslationTextComponent(this.getTranslationKey(stack) + ".tooltip"));
    }
}
