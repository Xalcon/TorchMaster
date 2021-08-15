package net.xalcon.torchmaster.common;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.xalcon.torchmaster.Torchmaster;

public class TorchmasterItemGroup extends CreativeModeTab
{
    public final static TorchmasterItemGroup INSTANCE = new TorchmasterItemGroup();

    private TorchmasterItemGroup()
    {
        super(Torchmaster.MODID);
    }

    @Override
    public ItemStack makeIcon() {
        return new ItemStack(ModBlocks.itemMegaTorch);
    }
}
