package net.xalcon.torchmaster.items;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.xalcon.torchmaster.Torchmaster;

import javax.annotation.Nullable;
import java.util.List;

public class TMItemBlock extends BlockItem
{
    public TMItemBlock(Block blockIn, Properties builder)
    {
        super(blockIn, builder);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag)
    {
        super.appendHoverText(stack, world, tooltip, flag);
        tooltip.add(Component.translatable(this.getDescriptionId(stack) + ".tooltip"));
    }
}
