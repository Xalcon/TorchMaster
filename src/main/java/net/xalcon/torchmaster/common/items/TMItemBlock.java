package net.xalcon.torchmaster.common.items;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;

public class TMItemBlock extends BlockItem
{
    public TMItemBlock(Block blockIn, Properties builder)
    {
        super(blockIn, builder);
        this.setRegistryName(blockIn.getRegistryName());
    }
}
