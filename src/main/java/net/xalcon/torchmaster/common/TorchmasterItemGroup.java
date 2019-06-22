package net.xalcon.torchmaster.common;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.xalcon.torchmaster.Torchmaster;

public class TorchmasterItemGroup extends ItemGroup
{
    public final static TorchmasterItemGroup INSTANCE = new TorchmasterItemGroup();

    private TorchmasterItemGroup()
    {
        super(Torchmaster.MODID);
    }

    @Override
    public ItemStack createIcon()
    {
        return new ItemStack(ModBlocks.itemMegaTorch);
    }
}
