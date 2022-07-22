package net.xalcon.torchmaster.common;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.xalcon.torchmaster.Constants;
import net.xalcon.torchmaster.ModRegistry;

public class TorchmasterCreativeTab extends CreativeModeTab
{
    public final static TorchmasterCreativeTab INSTANCE = new TorchmasterCreativeTab();

    private TorchmasterCreativeTab()
    {
        super(Constants.MOD_ID);
    }

    @Override
    public ItemStack makeIcon() {
        return new ItemStack(ModRegistry.itemMegaTorch.get());
    }
}
