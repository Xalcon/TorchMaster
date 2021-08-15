package net.xalcon.torchmaster.common.items;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
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
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag)
    {
        super.appendHoverText(stack, world, tooltip, flag);
        if(TorchmasterConfig.GENERAL.beginnerTooltips.get())
            tooltip.add(new TranslatableComponent(this.getDescriptionId(stack) + ".tooltip"));
    }
}
