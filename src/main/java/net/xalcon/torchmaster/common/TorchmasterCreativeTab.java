package net.xalcon.torchmaster.common;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.xalcon.torchmaster.Torchmaster;

public class TorchmasterCreativeTab extends CreativeModeTab
{
    public final static TorchmasterCreativeTab INSTANCE = new TorchmasterCreativeTab();

    private TorchmasterCreativeTab()
    {
        super(Torchmaster.MODID);
    }

    @Override
    public ItemStack makeIcon() {
        return new ItemStack(ModBlocks.itemMegaTorch.get());
    }
}
