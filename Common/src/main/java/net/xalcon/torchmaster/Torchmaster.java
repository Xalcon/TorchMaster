package net.xalcon.torchmaster;

import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.xalcon.torchmaster.config.ITorchmasterConfig;
import net.xalcon.torchmaster.platform.Services;
import org.apache.commons.lang3.NotImplementedException;

import java.util.List;

public class Torchmaster
{
    public static final EntityFilterRegistry MegaTorchFilterRegistry = new EntityFilterRegistry();
    public static final EntityFilterRegistry DreadLampFilterRegistry = new EntityFilterRegistry();

    // This method serves as an initialization hook for the mod. The vanilla
    // game has no mechanism to load tooltip listeners so this must be
    // invoked from a mod loader specific project like Forge or Fabric.
    public static void init() {

        Constants.LOG.info("Hello from Common init on {}! we are currently in a {} environment!", Services.PLATFORM.getPlatformName(), Services.PLATFORM.isDevelopmentEnvironment() ? "development" : "production");

        ModRegistry.init();
    }
}